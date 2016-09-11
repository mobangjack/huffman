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
package com.twogen.it.coder.util;

import java.util.HashSet;

/**
 * 十六进制字符型符号集（'0'~'F'）
 * @author 帮杰
 *
 */
@SuppressWarnings("serial")
public class HexCharSymbolSet extends HashSet<Character> {
	
	public HexCharSymbolSet() {
		add('0');
		add('1');
		add('2');
		add('3');
		add('4');
		add('5');
		add('6');
		add('7');
		add('8');
		add('9');
		add('A');
		add('B');
		add('C');
		add('D');
		add('E');
		add('F');
	}
}
