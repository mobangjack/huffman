/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twogen.it.coder;

import java.util.Arrays;
import java.util.Collection;

/**
 * 霍夫曼编码器
 * @author 帮杰
 *
 * @param <T> 码符号类型
 */
public class HuffmanCoder<T> implements Coder<T> {

	/**
	 * 基（等于符号集个数）
	 */
	private final int base;
	
	/**
	 * 符号集
	 */
	private final Collection<T> symbolSet;
	
	/**
	 * 是否随机编码
	 */
	private final boolean arbitrary;
	
	/**
	 * 是否优化
	 */
	private final boolean optimize;
	
	/**
	 * 最简单的构造器，指定符号集，采用固定编码，并提供优化
	 * @param symbolSet 符号集（个数应大于1）
	 */
	public HuffmanCoder(Collection<T> symbolSet) {
		this(symbolSet, false, true);
	}
	
	/**
	 * 含全部特性参数的构造器
	 * @param symbolSet 符号集（个数应大于1）
	 * @param arbitrary 是否随机编码（true：是；false：否）
	 * @param optimize 是否优化（true：是；false：否）
	 */
	public HuffmanCoder(Collection<T> symbolSet, boolean arbitrary, boolean optimize) {
		if(symbolSet==null || symbolSet.isEmpty()){
			throw new IllegalArgumentException("symbolSet can not be null or empty");
		}
		this.base = symbolSet.size();
		this.symbolSet = symbolSet;
		this.arbitrary = arbitrary;
		this.optimize = optimize;
	}
	
	@Override
	public CodeTree<T> code(double... probabilities) {
		//降序排序
		sort(probabilities);
		//预处理
		probabilities = prehandle(probabilities);
		//码树图
		CodeTreeMap<T> codeTreeMap = new CodeTreeMap<T>();
		//编码
		CodeTree<T> codeTree = code(probabilities, codeTreeMap);
		//返回码树
		return codeTree;
	}
	
	/**
	 * 降序排序
	 * @param probabilities 信源的概率分布
	 */
	private static void sort(double[] probabilities) {
		for (int i = 1; i < probabilities.length; i++) {
			for (int j = 0; j < probabilities.length-i; j++) {
				if (probabilities[j] < probabilities[j+1]) {
					double tmp = probabilities[j];
					probabilities[j] = probabilities[j+1];
					probabilities[j+1] = tmp;
				}
			}
		}
	}
	
	/**
	 * 预处理<br>
	 * 如果待编码符号数s不满足s=(base-1)*n+base，则填充最少的0使满足
	 * @param probabilities 信源的概率分布
	 * @return
	 */
	private double[] prehandle(double[] probabilities) {
		int m = (probabilities.length-base)%(base-1);
		if (m == 0){
			return probabilities;
		}
		int n = (probabilities.length-base)/(base-1);
		int s = (base-1)*(n+1)+base;
		double[] p = new double[s];
		for (int i = 0; i < probabilities.length; i++) {
			p[i] = probabilities[i];
		}
		return p;
	}
	
	/**
	 * 迭代编码，由下至上构建码树
	 * @param probabilities 信源的概率分布
	 * @param codeTreeMap 码树图
	 * @return 码树
	 */
	private CodeTree<T> code(double[] probabilities, CodeTreeMap<T> codeTreeMap) {
		//每一次迭代都生成新的码树
		CodeTree<T> codeTree = new CodeTree<T>();
		//符号挑选器
		SymbolPicker<T> symbolPicker = new SymbolPicker<T>(arbitrary ,symbolSet);
		//新信源符号的概率
		double probability = 0;
		for (int i = probabilities.length-1; i > probabilities.length-base-1; i--) {
			//累加生成新信源符号的概率
			probability += probabilities[i];
			//下一个符号
			T symbol = symbolPicker.next();
			//从码树图中取得游离的历史码树，放入新的码树中构建，同时将其移除（忘记）
			codeTree.put(symbol,codeTreeMap.remove(i));
		}
		//迭代直到信源大小不能再缩减为止
		if (probabilities.length == base) {
			return codeTree;
		}
		//缩减信源
		probabilities = reduce(probabilities);
		//找到新信源符号待插入位置
		int index = indexToInsert(probability, probabilities);
		//插入新信源符号
		insert(probabilities, probability, index);
		//调整码树图
		adjustCodeTreeMap(codeTreeMap, index);
		//记住新生成的码树
		codeTreeMap.put(index, codeTree);
		//继续下一轮编码
		return code(probabilities, codeTreeMap);
	}

	/**
	 * 缩减信源
	 * @param probabilities 信源概率分布
	 * @return 缩减后的信源概率分布
	 */
	private double[] reduce(double[] probabilities) {
		double[] p = new double[probabilities.length-base+1];
		for (int i = 0; i < p.length; i++) {
			p[i] = probabilities[i];
		}
		return p;
	}
	
	/**
	 * 找到新信源符号待插入位置
	 * @param probability 合并生成的新符号概率
	 * @param probabilities 信源概率分布
	 * @return 新信源符号概率待插入位置
	 */
	private int indexToInsert(double probability, double[] probabilities) {
		if (optimize) {
			for (int i = 0; i < probabilities.length; i++) {
				if (probability >= probabilities[i]) {
					return i;
				}
			}
			return probabilities.length-1;
		}else {
			for (int i = probabilities.length-2; i > -1; i--) {
				if (probability <= probabilities[i]) {
					return i+1;
				}
			}
			return 0;
		}
	}
	
	/**
	 * 插入新信源符号
	 * @param probabilities 信源概率分布
	 * @param probability 合并生成的新符号概率
	 * @param index 新信源符号概率待插入位置
	 */
	private void insert(double[] probabilities, double probability, int index) {
		for (int i = probabilities.length-1; i > index; i--) {
			probabilities[i] = probabilities[i-1];
		}
		probabilities[index] = probability;
	}
	
	/**
	 * 调整码树图
	 * @param codeTreeMap 码树图
	 * @param index 合并生成的新符号概率插入位置
	 * @return
	 */
	private void adjustCodeTreeMap(CodeTreeMap<T> codeTreeMap, int index) {
		if (!codeTreeMap.isEmpty()) {
			Integer[] indexs = codeTreeMap.keySet().toArray(new Integer[codeTreeMap.size()]);
			Arrays.sort(indexs);
			for (int i = indexs.length-1; i > -1; i--) {
				CodeTree<T> codeTree = codeTreeMap.remove(indexs[i]);
				codeTreeMap.put(indexs[i]+1, codeTree);
			}
		}
	}
	
	/**
	 * 获取编码器的基
	 * @return
	 */
	public int getBase() {
		return base;
	}

	/**
	 * 获取编码符号集
	 * @return
	 */
	public Collection<T> getSymbolSet() {
		return symbolSet;
	}

	/**
	 * 是否随机编码
	 * @return
	 */
	public boolean isArbitrary() {
		return arbitrary;
	}

	/**
	 * 是否优化
	 * @return
	 */
	public boolean isOptimize() {
		return optimize;
	}
	
}
