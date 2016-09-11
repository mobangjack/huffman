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
import java.util.ArrayList;

/**
 * 单条编码路径
 * @author 帮杰
 *
 * @param <T> 码符号类型
 */
@SuppressWarnings("serial")
public class Path<T> extends ArrayList<T> implements Comparable<Path<T>> {

	@Override
	public int compareTo(Path<T> path) {
		return (size()-path.size());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(size());
		for (T e : this) {
			sb.append(e);
		}
		return sb.toString();
	}
}
