begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|wocompat
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|wocompat
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_comment
comment|/**  * A<b>PropertyListSerialization</b> is a utility class that reads and stores files in  * NeXT/Apple property list format. Unlike corresponding WebObjects class,  *<code>PropertyListSerialization</code> uses standard Java collections (lists and  * maps) to store property lists.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|PropertyListSerialization
block|{
comment|/**      * Reads a property list file. Returns a property list object, that is normally a      * java.util.List or a java.util.Map, but can also be a String or a Number.      */
specifier|public
specifier|static
name|Object
name|propertyListFromFile
parameter_list|(
name|File
name|f
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|propertyListFromFile
argument_list|(
name|f
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Reads a property list file. Returns a property list object, that is normally a      * java.util.List or a java.util.Map, but can also be a String or a Number.      */
specifier|public
specifier|static
name|Object
name|propertyListFromFile
parameter_list|(
name|File
name|f
parameter_list|,
name|PlistDataStructureFactory
name|factory
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"No such file: "
operator|+
name|f
argument_list|)
throw|;
block|}
return|return
operator|new
name|Parser
argument_list|(
name|f
argument_list|,
name|factory
argument_list|)
operator|.
name|propertyList
argument_list|()
return|;
block|}
comment|/**      * Reads a property list data from InputStream. Returns a property list o bject, that      * is normally a java.util.List or a java.util.Map, but can also be a String or a      * Number.      */
specifier|public
specifier|static
name|Object
name|propertyListFromStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
return|return
name|propertyListFromStream
argument_list|(
name|in
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Reads a property list data from InputStream. Returns a property list o bject, that      * is normally a java.util.List or a java.util.Map, but can also be a String or a      * Number.      */
specifier|public
specifier|static
name|Object
name|propertyListFromStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|PlistDataStructureFactory
name|factory
parameter_list|)
block|{
return|return
operator|new
name|Parser
argument_list|(
name|in
argument_list|,
name|factory
argument_list|)
operator|.
name|propertyList
argument_list|()
return|;
block|}
comment|/**      * Saves property list to file.      */
specifier|public
specifier|static
name|void
name|propertyListToFile
parameter_list|(
name|File
name|f
parameter_list|,
name|Object
name|plist
parameter_list|)
block|{
try|try
block|{
name|BufferedWriter
name|out
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|writeObject
argument_list|(
literal|""
argument_list|,
name|out
argument_list|,
name|plist
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error saving plist."
argument_list|,
name|ioex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Saves property list to file.      */
specifier|public
specifier|static
name|void
name|propertyListToStream
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|Object
name|plist
parameter_list|)
block|{
try|try
block|{
name|BufferedWriter
name|out
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|writeObject
argument_list|(
literal|""
argument_list|,
name|out
argument_list|,
name|plist
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error saving plist."
argument_list|,
name|ioex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Internal method to recursively write a property list object.      */
specifier|protected
specifier|static
name|void
name|writeObject
parameter_list|(
name|String
name|offset
parameter_list|,
name|Writer
name|out
parameter_list|,
name|Object
name|plist
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|plist
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|plist
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
name|list
init|=
operator|(
name|Collection
operator|)
name|plist
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|offset
argument_list|)
expr_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
return|return;
block|}
name|out
operator|.
name|write
argument_list|(
literal|"(\n"
argument_list|)
expr_stmt|;
name|String
name|childOffset
init|=
name|offset
operator|+
literal|"   "
decl_stmt|;
name|Iterator
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|appended
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// Java collections can contain nulls, skip them
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|appended
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|", \n"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
name|childOffset
argument_list|)
expr_stmt|;
name|writeObject
argument_list|(
name|childOffset
argument_list|,
name|out
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|appended
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|plist
operator|instanceof
name|Map
condition|)
block|{
name|Map
name|map
init|=
operator|(
name|Map
operator|)
name|plist
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|offset
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
literal|"{}"
argument_list|)
expr_stmt|;
return|return;
block|}
name|out
operator|.
name|write
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|String
name|childOffset
init|=
name|offset
operator|+
literal|"    "
decl_stmt|;
name|Iterator
name|it
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// Java collections can contain nulls, skip them
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Object
name|obj
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|childOffset
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|quoteString
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
name|writeObject
argument_list|(
name|childOffset
argument_list|,
name|out
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|plist
operator|instanceof
name|String
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|quoteString
argument_list|(
name|plist
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|plist
operator|instanceof
name|Number
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|plist
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported class for property list serialization: "
operator|+
name|plist
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Escapes all doublequotes and backslashes.      */
specifier|protected
specifier|static
name|String
name|escapeString
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
name|str
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|chars
operator|.
name|length
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
name|len
operator|+
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|chars
index|[
name|i
index|]
operator|==
literal|'\"'
operator|||
name|chars
index|[
name|i
index|]
operator|==
literal|'\\'
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'\\'
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|chars
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a quoted String, with all the escapes preprocessed. May return an unquoted      * String if it contains no special characters. The rule for a non-special character      * is the following:      *       *<pre>      *       c&gt;= 'a'&amp;&amp; c&lt;= 'z'      *       c&gt;= 'A'&amp;&amp; c&lt;= 'Z'      *       c&gt;= '0'&amp;&amp; c&lt;= '9'      *       c == '_'      *       c == '$'      *       c == ':'      *       c == '.'      *       c == '/'      *</pre>      */
specifier|protected
specifier|static
name|String
name|quoteString
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|boolean
name|shouldQuote
init|=
literal|false
decl_stmt|;
comment|// scan string for special chars,
comment|// if we have them, string must be quoted
name|String
name|noQuoteExtras
init|=
literal|"_$:./"
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|str
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|chars
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
name|shouldQuote
operator|=
literal|true
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|!
name|shouldQuote
operator|&&
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|chars
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'0'
operator|&&
name|c
operator|<=
literal|'9'
operator|)
operator|||
name|noQuoteExtras
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|>=
literal|0
condition|)
block|{
continue|continue;
block|}
name|shouldQuote
operator|=
literal|true
expr_stmt|;
block|}
name|str
operator|=
name|escapeString
argument_list|(
name|str
argument_list|)
expr_stmt|;
return|return
operator|(
name|shouldQuote
operator|)
condition|?
literal|'\"'
operator|+
name|str
operator|+
literal|'\"'
else|:
name|str
return|;
block|}
block|}
end_class

end_unit

