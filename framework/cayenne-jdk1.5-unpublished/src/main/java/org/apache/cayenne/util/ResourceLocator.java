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
name|util
package|;
end_package

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
name|FileInputStream
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
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|conf
operator|.
name|ResourceFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Utility class to find resources (files, etc.), using a preconfigured strategy.  *   */
end_comment

begin_class
specifier|public
class|class
name|ResourceLocator
implements|implements
name|ResourceFinder
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// properties for enabling/disabling certain lookup strategies
specifier|protected
name|boolean
name|skipAbsolutePath
decl_stmt|;
specifier|protected
name|boolean
name|skipClasspath
decl_stmt|;
specifier|protected
name|boolean
name|skipCurrentDirectory
decl_stmt|;
specifier|protected
name|boolean
name|skipHomeDirectory
decl_stmt|;
comment|// additional lookup paths (as Strings)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|additionalClassPaths
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|additionalFilesystemPaths
decl_stmt|;
comment|// ClassLoader used for resource loading
specifier|protected
name|ClassLoader
name|classLoader
decl_stmt|;
comment|/**      * Returns a resource as InputStream if it is found in CLASSPATH or<code>null</code>      * otherwise. Lookup is normally performed in all JAR and ZIP files and directories      * available to the ClassLoader.      *       * @deprecated since 3.0 unused.      */
specifier|public
specifier|static
name|InputStream
name|findResourceInClasspath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|URL
name|url
init|=
name|findURLInClasspath
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"resource found in classpath: "
operator|+
name|url
argument_list|)
expr_stmt|;
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"resource not found in classpath: "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns a resource as InputStream if it is found in the filesystem or      *<code>null</code> otherwise. Lookup is first performed relative to the user's      * home directory (as defined by "user.home" system property), and then relative to      * the current directory.      *       * @deprecated since 3.0 unused      */
specifier|public
specifier|static
name|InputStream
name|findResourceInFileSystem
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|File
name|file
init|=
name|findFileInFileSystem
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"resource found in file system: "
operator|+
name|file
argument_list|)
expr_stmt|;
return|return
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
return|;
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"resource not found in file system: "
operator|+
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Looks up a file in the filesystem. First looks in the user home directory, then in      * the current directory.      *       * @return file object matching the name, or null if file can not be found or if it is      *         not readable.      * @see #findFileInHomeDirectory(String)      * @see #findFileInCurrentDirectory(String)      */
specifier|public
specifier|static
name|File
name|findFileInFileSystem
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|File
name|file
init|=
name|findFileInHomeDirectory
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
name|file
operator|=
name|findFileInCurrentDirectory
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"file found in file system: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"file not found in file system: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Looks up a file in the user home directory.      *       * @return file object matching the name, or<code>null</code> if<code>file</code>      *         cannot be found or is not readable.      */
specifier|public
specifier|static
name|File
name|findFileInHomeDirectory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// look in home directory
name|String
name|homeDirPath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
name|name
decl_stmt|;
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|homeDirPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"file found in home directory: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|=
literal|null
expr_stmt|;
name|logObj
operator|.
name|debug
argument_list|(
literal|"file not found in home directory: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"permission denied reading file: "
operator|+
name|homeDirPath
argument_list|,
name|se
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Looks up a file in the current directory.      *       * @return file object matching the name, or<code>null</code> if<code>file</code>      *         can not be found is not readable.      */
specifier|public
specifier|static
name|File
name|findFileInCurrentDirectory
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// look in the current directory
name|String
name|currentDirPath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
name|name
decl_stmt|;
try|try
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|currentDirPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"file found in current directory: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"file not found in current directory: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|file
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|se
parameter_list|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"permission denied reading file: "
operator|+
name|currentDirPath
argument_list|,
name|se
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Looks up the URL for the named resource using this class' ClassLoader.      */
specifier|public
specifier|static
name|URL
name|findURLInClasspath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ClassLoader
name|classLoader
init|=
name|ResourceLocator
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|classLoader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|findURLInClassLoader
argument_list|(
name|name
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
comment|/**      * Looks up the URL for the named resource using the specified ClassLoader.      */
specifier|public
specifier|static
name|URL
name|findURLInClassLoader
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|URL
name|url
init|=
name|loader
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"URL found with classloader: "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"URL not found with classloader: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
comment|/**      * Returns a base URL as a String from which this class was loaded. This is normally a      * JAR or a file URL, but it is ClassLoader dependent.      *       * @deprecated since 3.0 unused.      */
specifier|public
specifier|static
name|String
name|classBaseUrl
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
name|String
name|pathToClass
init|=
name|aClass
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|".class"
decl_stmt|;
name|ClassLoader
name|classLoader
init|=
name|aClass
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|classLoader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
name|URL
name|selfUrl
init|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|pathToClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|selfUrl
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|urlString
init|=
name|selfUrl
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
return|return
name|urlString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|urlString
operator|.
name|length
argument_list|()
operator|-
name|pathToClass
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Creates new ResourceLocator with default lookup policy including user home      * directory, current directory and CLASSPATH.      */
specifier|public
name|ResourceLocator
parameter_list|()
block|{
name|this
operator|.
name|additionalClassPaths
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|additionalFilesystemPaths
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an InputStream on the found resource using the lookup strategy configured      * for this ResourceLocator or<code>null</code> if no readable resource can be      * found for the given name.      */
specifier|public
name|InputStream
name|findResourceStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|url
init|=
name|findResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"Error reading URL, ignoring"
argument_list|,
name|ioex
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * @since 3.0      */
specifier|public
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|findResource
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Collection
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|resource
init|=
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|resource
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|singleton
argument_list|(
name|resource
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|URL
operator|>
name|emptySet
argument_list|()
return|;
block|}
comment|/**      * Returns a resource URL using the lookup strategy configured for this      * Resourcelocator or<code>null</code> if no readable resource can be found for the      * given name.      */
specifier|public
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
operator|!
name|willSkipAbsolutePath
argument_list|()
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isAbsolute
argument_list|()
operator|&&
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"File found at absolute path: "
operator|+
name|name
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|f
operator|.
name|toURL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed url, ignoring."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"No file at absolute path: "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|willSkipHomeDirectory
argument_list|()
condition|)
block|{
name|File
name|f
init|=
name|findFileInHomeDirectory
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|f
operator|.
name|toURL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed url, ignoring"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|willSkipCurrentDirectory
argument_list|()
condition|)
block|{
name|File
name|f
init|=
name|findFileInCurrentDirectory
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|f
operator|.
name|toURL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed url, ignoring"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|additionalFilesystemPaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"searching additional paths: "
operator|+
name|this
operator|.
name|additionalFilesystemPaths
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|filePath
range|:
name|this
operator|.
name|additionalFilesystemPaths
control|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|filePath
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|logObj
operator|.
name|debug
argument_list|(
literal|"searching for: "
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|f
operator|.
name|toURL
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed URL, ignoring."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|willSkipClasspath
argument_list|()
condition|)
block|{
comment|// start with custom classpaths and then move to the default one
if|if
condition|(
operator|!
name|this
operator|.
name|additionalClassPaths
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logObj
operator|.
name|debug
argument_list|(
literal|"searching additional classpaths: "
operator|+
name|this
operator|.
name|additionalClassPaths
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|classPath
range|:
name|this
operator|.
name|additionalClassPaths
control|)
block|{
name|String
name|fullName
init|=
name|classPath
operator|+
literal|"/"
operator|+
name|name
decl_stmt|;
name|logObj
operator|.
name|debug
argument_list|(
literal|"searching for: "
operator|+
name|fullName
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|findURLInClassLoader
argument_list|(
name|fullName
argument_list|,
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
return|return
name|url
return|;
block|}
block|}
block|}
name|URL
name|url
init|=
name|findURLInClassLoader
argument_list|(
name|name
argument_list|,
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
return|return
name|url
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a directory resource URL using the lookup strategy configured for this      * ResourceLocator or<code>null</code> if no readable resource can be found for the      * given name. The returned resource is assumed to be a directory, so the returned URL      * will be in a directory format (with "/" at the end).      */
specifier|public
name|URL
name|findDirectoryResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|URL
name|url
init|=
name|findResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|String
name|urlSt
init|=
name|url
operator|.
name|toExternalForm
argument_list|()
decl_stmt|;
return|return
operator|(
name|urlSt
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|)
condition|?
name|url
else|:
operator|new
name|URL
argument_list|(
name|urlSt
operator|+
literal|"/"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring...
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed URL, ignoring."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns true if no lookups are performed in the user home directory.      */
specifier|public
name|boolean
name|willSkipHomeDirectory
parameter_list|()
block|{
return|return
name|skipHomeDirectory
return|;
block|}
comment|/**      * Sets "skipHomeDirectory" property.      */
specifier|public
name|void
name|setSkipHomeDirectory
parameter_list|(
name|boolean
name|skipHomeDir
parameter_list|)
block|{
name|this
operator|.
name|skipHomeDirectory
operator|=
name|skipHomeDir
expr_stmt|;
block|}
comment|/**      * Returns true if no lookups are performed in the current directory.      */
specifier|public
name|boolean
name|willSkipCurrentDirectory
parameter_list|()
block|{
return|return
name|skipCurrentDirectory
return|;
block|}
comment|/**      * Sets "skipCurrentDirectory" property.      */
specifier|public
name|void
name|setSkipCurrentDirectory
parameter_list|(
name|boolean
name|skipCurDir
parameter_list|)
block|{
name|this
operator|.
name|skipCurrentDirectory
operator|=
name|skipCurDir
expr_stmt|;
block|}
comment|/**      * Returns true if no lookups are performed in the classpath.      */
specifier|public
name|boolean
name|willSkipClasspath
parameter_list|()
block|{
return|return
name|skipClasspath
return|;
block|}
comment|/**      * Sets "skipClasspath" property.      */
specifier|public
name|void
name|setSkipClasspath
parameter_list|(
name|boolean
name|skipClasspath
parameter_list|)
block|{
name|this
operator|.
name|skipClasspath
operator|=
name|skipClasspath
expr_stmt|;
block|}
comment|/**      * Returns the ClassLoader associated with this ResourceLocator.      */
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|this
operator|.
name|classLoader
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
return|return
name|loader
return|;
block|}
comment|/**      * Sets ClassLoader used to locate resources. If<code>null</code> is passed, the      * ClassLoader of the ResourceLocator class will be used.      */
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
comment|/**      * Returns true if no lookups are performed using path as absolute path.      */
specifier|public
name|boolean
name|willSkipAbsolutePath
parameter_list|()
block|{
return|return
name|skipAbsolutePath
return|;
block|}
comment|/**      * Sets "skipAbsolutePath" property.      */
specifier|public
name|void
name|setSkipAbsolutePath
parameter_list|(
name|boolean
name|skipAbsPath
parameter_list|)
block|{
name|this
operator|.
name|skipAbsolutePath
operator|=
name|skipAbsPath
expr_stmt|;
block|}
comment|/**      * Adds a custom path for class path lookups. Format should be "my/package/name"      *<i>without</i> leading "/".      */
specifier|public
name|void
name|addClassPath
parameter_list|(
name|String
name|customPath
parameter_list|)
block|{
name|this
operator|.
name|additionalClassPaths
operator|.
name|add
argument_list|(
name|customPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given String as a custom path for filesystem lookups. The path can be      * relative or absolute and is<i>not</i> checked for existence.      *       * @throws IllegalArgumentException if<code>path</code> is<code>null</code>.      */
specifier|public
name|void
name|addFilesystemPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|additionalFilesystemPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Path must not be null."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Adds the given directory as a path for filesystem lookups. The directory is checked      * for existence.      *       * @throws IllegalArgumentException if<code>path</code> is<code>null</code>,      *             not a directory or not readable.      */
specifier|public
name|void
name|addFilesystemPath
parameter_list|(
name|File
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|this
operator|.
name|addFilesystemPath
argument_list|(
name|path
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Path '"
operator|+
name|path
operator|+
literal|"' is not a directory."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

