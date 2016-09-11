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
package com.twogen.it.coder.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.twogen.it.coder.CodeAnalyzer;
import com.twogen.it.coder.CodeTree;
import com.twogen.it.coder.HuffmanCoder;
import com.twogen.it.coder.InfoSource;
import com.twogen.it.coder.util.DigitCharSymbolSet;

/**
 * 实验内容：<br>
 * 1、给出信源符号的概率统计分布，并计算信源熵<br>
 * 2、给出两种以上单信源符号Huffman码表<br>
 * 3、计算每信源的平均字长，并与信源熵比较<br>
 * 4、计算编码效率，分析码长方差对实验系统的影响<br>
 * @author 帮杰
 *
 */
public class Main {
	
	public static final String FILE_PATH = "G:\\java\\project\\huffman\\src\\com\\twogen\\it\\coder\\test\\article.txt";
	public static final int BASE = 2;
	public static final Collection<Character> SYMBOLSET = new DigitCharSymbolSet(BASE);
	
	public static void main(String[] args) {
		double[] probabilities = statistic(new File(FILE_PATH));
		//double[] probabilities = new double[]{0.22117420596727622,0.09201154956689124,0.06621751684311838,0.0629451395572666,0.05890279114533205,0.05370548604427334,0.0465832531280077,0.04639076034648701,0.0411934552454283,0.04042348411934552,0.03734359961501444,0.03599615014436958,0.02271414821944177,0.020981713185755535,0.019826756496631376,0.019249278152069296,0.01886429258902791,0.01616939364773821,0.015399422521655439,0.012704523580365737,0.007892204042348411,0.006159769008662175,0.006159769008662175,0.0046198267564966315,0.0038498556304138597,0.0034648700673724736,0.0034648700673724736,0.0015399422521655437,0.0015399422521655437,0.0015399422521655437,0.001347449470644851,0.0011549566891241579,0.0011549566891241579,9.624639076034649E-4,7.699711260827719E-4,7.699711260827719E-4,5.774783445620789E-4,5.774783445620789E-4,3.8498556304138594E-4,3.8498556304138594E-4,3.8498556304138594E-4,3.8498556304138594E-4,3.8498556304138594E-4,3.8498556304138594E-4,3.8498556304138594E-4,1.9249278152069297E-4,1.9249278152069297E-4,1.9249278152069297E-4,1.9249278152069297E-4,1.9249278152069297E-4};
		//double[] probabilities = new double[]{0.4,0.2,0.2,0.1,0.1};
		//double[] probabilities = new double[]{0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
		//降序排序
		sort(probabilities);
		//预处理
		probabilities = prehandle(BASE, probabilities);
		//构造信源
		InfoSource infoSource = new InfoSource(probabilities);
		
		//给出信源概率统计分布
		println("p-distribution:");
		println(infoSource);
		println();
		
		//计算信源熵
		double entrophy = infoSource.entrophy(BASE);
		println("entrophy:");
		println(entrophy);
		println();
		
		//给出两种以上单信源符号Huffman码表
		for (int i = 0; i < 1; i++) {
			println("\t\t\t\tcode-table("+i+")");
			HuffmanCoder<Character> coder = new HuffmanCoder<Character>(SYMBOLSET, true, true);
			CodeTree<Character> codeTree = coder.code(probabilities);
			CodeAnalyzer<Character> codeAnalyzer = new CodeAnalyzer<Character>(BASE, infoSource, codeTree.paths());
			println(codeAnalyzer);
		}
	}
	
	/**
	 * 从文本文件中统计出信源分布。统计26个英文字母，其他当空格处理。
	 * @param file
	 * @return
	 */
	public static double[] statistic(File file) {
		InputStream in = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			in = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			println("original source:");
			println(sb);
			println();
			return statistic(sb.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 从原始字符串中统计出信源分布。统计26个英文字母，其他当空格处理。
	 * @param s
	 * @return
	 */
	public static double[] statistic(String s) {
		Map<Character, Double> map = new HashMap<Character, Double>();
		int sum = 0;
		for (int i = 0; i < s.length(); i++) {
			Character c = s.charAt(i);
			if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
				c = ' ';
			}
			Double f = map.get(c);
			if (f == null) {
				map.put(c, 1.0);
			}else {
				map.put(c, f+1);
			}
			sum++;
		}
		println("statistic:");
		println(map);
		println();
		double[] probabilities = new double[map.size()];
		int i = 0;
		for (Double p : map.values()) {
			probabilities[i] = p/sum;
			i++;
		}
		return probabilities;
	}
	
	/**
	 * 预处理<br>
	 * 如果待编码符号数s不满足s=(base-1)*m+base，则填充最少的0使满足
	 * @param probabilities
	 * @return
	 */
	public static double[] prehandle(int base, double... probabilities) {
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
	 * 降序排序
	 * @param probabilities
	 */
	public static void sort(double[] probabilities) {
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
	 * 打印
	 */
	public static void println(Object o) {
		System.out.println(o);
	}
	
	public static void println() {
		System.out.println();
	}
}
