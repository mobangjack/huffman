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



/**
 * 码分析器
 * @author 帮杰
 *
 * @param <T>码符号类型
 */
public class CodeAnalyzer<T> {

	/**
	 * 基
	 */
	private final int base;
	
	/**
	 * 信源
	 */
	private final InfoSource infoSource;
	
	/**
	 * 编码生成的路径
	 */
	private final Paths<T> paths;
	
	/**
	 * 平均码长
	 */
	private Double averageCodeLength;
	
	/**
	 * 码长方差
	 */
	private Double codeLengthVariance;
	
	/**
	 * 编码效率
	 */
	private Double codeEfficiency;
	
	/**
	 * 码分析器构造函数
	 * @param infoSource 信源
	 * @param paths 编码生成的路径
	 */
	public CodeAnalyzer(int base, InfoSource infoSource, Paths<T> paths) {
		this.base = base;
		this.infoSource = infoSource;
		this.paths = paths;
	}

	/**
	 * 获得基
	 * @return
	 */
	public int getBase() {
		return base;
	}

	/**
	 * 获得信源
	 * @return
	 */
	public InfoSource getInfoSource() {
		return infoSource;
	}

	/**
	 * 获得编码生成的路径
	 * @return
	 */
	public Paths<T> getPaths() {
		return paths;
	}

	/**
	 * 获得平均码长
	 * @return
	 */
	public Double getAverageCodeLength() {
		if (averageCodeLength == null) {
			averageCodeLength = 0.0;
			double[] probabilities = infoSource.getProbabilities();
			for (int i = 0; i < probabilities.length; i++) {
				averageCodeLength += (probabilities[i]*paths.get(i).size());
			}
		}
		return averageCodeLength;
	}
	
	/**
	 * 获得码长方差
	 * @return
	 */
	public Double getCodeLengthVariance() {
		if (codeLengthVariance == null) {
			codeLengthVariance = 0.0;
			double[] probabilities = infoSource.getProbabilities();
			for (int i = 0; i < probabilities.length; i++) {
				double probability = probabilities[i];
				codeLengthVariance += (probability*Math.pow((paths.get(i).size()-getAverageCodeLength()),2));
			}
		}
		return codeLengthVariance;
	}
	
	/**
	 * 获得编码效率
	 * @param base 基
	 * @return
	 */
	public Double getCodeEfficientcy() {
		if (codeEfficiency == null) {
			codeEfficiency = 100*infoSource.entrophy(base)/getAverageCodeLength();
		}
		return codeEfficiency;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		double[] probabilities = infoSource.getProbabilities();
		appendln(sb);
		sb.append("|\tprobability\t|\tcode-word\t|\tcode-word length\t");
		sb.append("\n");
		appendln(sb);
		for (int i = 0; i < probabilities.length; i++) {
			double p = probabilities[i];
			Path<T> path = paths.get(i);
			sb.append("|\t"+p+"\t|\t"+path+"\t|\t"+path.size());
			sb.append("\n");
			appendln(sb);
		}
		sb.append("|\taverage code-word length:"+getAverageCodeLength()+"\t");
		sb.append("\n");
		appendln(sb);
		sb.append("|\tcode-word length variance:"+getCodeLengthVariance()+"\t");
		sb.append("\n");
		appendln(sb);
		sb.append("|\tcode efficiency:"+getCodeEfficientcy()+"%\t");
		sb.append("\n");
		appendln(sb);
		return sb.toString();
	}
	
	private static void appendln(StringBuilder sb) {
		sb.append(" --------------------------------------------------------------- ");
		sb.append("\n");
	}
}
