/*
 * Copyright (C) 2013 Stefan Moschinski
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.devmond.shell;

/**
 * A class implementing this interface is responsible for handle the output of
 * the commands. For instance, writing it to the console.
 * 
 */
public interface Output {

	/**
	 * Like {@link #print(Object)} but adds a newline after the content of the
	 * object was written
	 * 
	 * @param output
	 * @see #print(Object)
	 */
	void println(Object output);
	
	/**
	 * @param output
	 *            {@link Object} that should be written to the output target; to
	 *            write the output {@link Object#toString()) is called
	 */
	void print(Object output);

	/**
	 * Close the output, further calls to {@link #print(Object)} or
	 * {@link #println(Object)} are not allowed
	 */
	void close();
}
