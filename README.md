Mondshell
=
Rationale
-
The MondShell is a plugin for Eclipse. The developement started as part of the 3rd Eclipse Hackathon in Dresden (April 20th, 2013). The plugin enhances the OSGi host console of Eclpse with additional commands. Unlike other shell plugins for Eclipse (such as <a href="https://code.google.com/p/elt/">Terminal plug-in for Eclipse</a>), it does not bring OS shell capabilities to Eclipse. Rather, it brings about commands that should make developer's work more convenient.
Commands
-
<ul>
<li><b>refresh</b> <i>projectName1</i> <i>projectName2</i> <i>projectNameX</i> - refreshes the given projects, if none is given, the whole Eclipse workspace is refreshed</li>
<li><b>match</b> <i>[-options]</i> <i>input</i> <i>regex</i> - checks whether the <i>input</i> matches the given regular expression
<ul><li><i>options</i>:<ul><li>-part - the regex does not need to match the whole input</li><li>-groups - shows the matched groups if there are any given in the regex</li><li>-flags - let you define regex flags, currently supported are <i>insensitive</i> and <i>canon</i> (if you want to state both, you have to separate the by a comma, e.g., <i>-flags insensitive,canon</i></li></ul></li></ul></li>
<li><b>encode</b> <i>[-options]</i> <i>encodingType</i> <i>stringToEncode</i> - let you encode the given <i>stringToEncode</i> using the URL, Base64 or HTML encoding<ul><li><i>options</i>:<ul><li>-charset <i>charsetToUse</i> - defines the charset to use for the encoding, the default is UTF-8</li></ul></li><li><i>encodingType</i>:<ul><li>html - the <i>stringToEncode</i> is encoded using HTML encoding</li><li>base64 - the <i>stringToEncode</i> is encoded using Base64 encoding</li><li>url - the <i>stringToEncode</i> is encoded using URL encoding</li></ul></li></ul></li>
<li><b>decode</b> <i>[-options]</i> <i>decodingType</i> <i>stringToDecode</i> - let you decode the given <i>stringToDecode</i> using the URL or Base64 decoding<ul><li><i>options</i>:<ul><li>-charset <i>charsetToUse</i> - defines the charset to use for the decoding, the default is UTF-8</li></ul></li><li><i>decodingType</i>:<ul><li>base64 - the <i>stringToDecode</i> is decoded using Base64 decoding</li><li>url - the <i>stringToDecode</i> is decoded using URL decoding</li></ul></li></ul></li>
<li><b>format</b> <i>dataType</i> <i>stringOrPath</i> - let you format XML or JSON documents into a human readable form<ul><li><i>dataType</i>:<ul><li>XML: the data type of the the given <i>stringOrPath</i> is XML</li><li>JSON: the data type of the the given <i>stringOrPath</i> is JSON</li></ul></li><li><i>stringOrPath</i>: let you pass an unformatted XML or JSON string or a path to a document that contains XML/JSON</li></ul></li>
<li><b>random</b> <i>[randomType]</i> - generates a random value depending on the <i>randomType</i>, if no <i>randomType</i> is given a random integer number is generated<ul><li><i>randomType</i>:<ul><li>uuid: generates a random UUID</li><li>double: creates a random double number between 0.0 (inclusive) and 1.0 (exclusive)</li><li>int: (from JavaDoc) Returns the next pseudorandom, uniformly distributed int value from this random number generator's sequence. The general contract of nextInt is that one int value is pseudorandomly generated and returned. All 232 possible int values are produced with (approximately) equal probability.</li></ul></li></ul></li>
<li><b>portuse</b> <i>portNumber</i> - Finds the process that uses the given <i>portNumber</i> and returns the name and the ID of the process (<b>currently Windows only!</b>)</li>
<li><b>freeport</b> <i>portNumber</i> - Finds the process that uses the given <i>portNumber</i> and tries to kill this process (<b>currently Windows only!</b>)</li>
<li><b>kill</b> <i>PID</i> - Tries to kill the process with the given <i>PID</i> (<b>currently Windows only!</b>)</li>
</ul>
### Custom Output
By default the output of the commands (if any) is written to the OSGi console. However, using <i>\> pathToFile</i> at the end of your command, you can define a file to which the output should be written. For instance, <i>random uuid \> C:\random.txt</i> writes the generated random UUID to the file <i>C:\random.txt</i>.
### Remote Access
Because the MondShell plugin is an extension of the OSGi host console it can be accessed via telnet if Eclipse is started with the system property -Dosgi.console=<i>\<PORT_NUMBER\></i>. 
### License
The MondShell plugin is licensed under the Apache Software License 2.0.
