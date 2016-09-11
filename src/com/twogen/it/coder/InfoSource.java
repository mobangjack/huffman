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
 * 信源
 * @author 帮杰
 */
public class InfoSource {

	/**
	 * 信源概率分布
	 */
	private final double[] probabilities;
	
	/**
	 * 构造函数
	 * @param probabilities 信源概率分布
	 */
	public InfoSource(double... probabilities) {
		this.probabilities = probabilities;
	}

	/**
	 * 将信源概率分布降序排序
	 */
	public void sort() {
		sort(probabilities);
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
	 * 计算信源熵
	 * @return H(X)
	 */
	public double entrophy() {
		return entrophy(2, probabilities);
	}
	
	/**
	 * 计算信源熵
	 * @param base 基
	 * @return 信源熵H(X)
	 */
	public double entrophy(int base) {
		return entrophy(base, probabilities);
	}
	
	/**
	 * 计算信源熵
	 * @param base 基
	 * @param probabilities 概率分布
	 * @return 信源熵H(X)
	 */
	public static double entrophy(int base, double... probabilities) {
		double entrophy = 0;
		for (int i = 0; i < probabilities.length; i++) {
			double probability = probabilities[i];
			if (probability > 0) {
				entrophy += (-probability*log(base, probability));
			}
		}
		return entrophy;
	}
	
	/**
	 * 换底
	 * @param x 底
	 * @param y 指数
	 * @return log<sub>x</sub>y
	 */
	private static double log(double x, double y) {
		return Math.log(y)/Math.log(x);
	}
	
	/**
	 * 获取信源概率分布
	 * @return
	 */
	public double[] getProbabilities() {
		return probabilities;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < probabilities.length; i++) {
			double p = probabilities[i];
			sb.append(p).append(",");
		}
		int i = sb.lastIndexOf(",");
		sb.replace(i, i+1, "]");
		return sb.toString();
	}
}
