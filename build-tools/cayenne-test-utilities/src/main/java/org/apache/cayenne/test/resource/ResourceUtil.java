begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|test
operator|.
name|resource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|FileOutputStream
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceUtil
block|{
comment|/** 	 * Copies resources to a file, thus making it available to the caller as 	 * File. 	 */
specifier|public
specifier|static
name|void
name|copyResourceToFile
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|URL
name|in
init|=
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|copyResourceToFile
argument_list|(
name|in
argument_list|,
name|file
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error copying resource to file : "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
comment|/** 	 * Returns a guaranteed non-null resource for a given name. 	 */
specifier|public
specifier|static
name|URL
name|getResource
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|relativeTo
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|URL
name|in
init|=
name|relativeTo
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Resource not found: "
operator|+
name|name
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|getResource
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/** 	 * Returns a guaranteed non-null resource for a given name. 	 */
specifier|public
specifier|static
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|in
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Resource not found: "
operator|+
name|name
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|getResource
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/** 	 * Returns a guaranteed non-null resource for a given name. 	 */
specifier|private
specifier|static
name|URL
name|getResource
parameter_list|(
name|URL
name|classloaderUrl
parameter_list|)
block|{
if|if
condition|(
name|classloaderUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null URL"
argument_list|)
throw|;
block|}
comment|// Fix for the issue described at
comment|// https://issues.apache.org/struts/browse/SB-35
comment|// Basically, spaces in filenames make maven cry.
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|classloaderUrl
operator|.
name|toExternalForm
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"%20"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error constructing URL."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|copyResourceToFile
parameter_list|(
name|URL
name|from
parameter_list|,
name|File
name|to
parameter_list|)
block|{
name|File
name|dir
init|=
name|to
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|int
name|bufSize
init|=
literal|8
operator|*
literal|1024
decl_stmt|;
try|try
init|(
name|BufferedInputStream
name|urlin
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|from
operator|.
name|openConnection
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|bufSize
argument_list|)
init|;
init|)
block|{
try|try
init|(
name|BufferedOutputStream
name|fout
init|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|to
argument_list|)
argument_list|,
name|bufSize
argument_list|)
init|;
init|)
block|{
name|copyPipe
argument_list|(
name|urlin
argument_list|,
name|fout
argument_list|,
name|bufSize
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|sx
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
name|void
name|copyPipe
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|,
name|int
name|bufSizeHint
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|read
init|=
operator|-
literal|1
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
name|bufSizeHint
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|bufSizeHint
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

