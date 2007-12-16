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
name|modeler
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|lang
operator|.
name|SystemUtils
import|;
end_import

begin_comment
comment|/**  * Opens a URL in the system default browser.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|BrowserControl
block|{
comment|// The default system browser under windows.
specifier|private
specifier|static
specifier|final
name|String
name|WIN_PATH
init|=
literal|"rundll32"
decl_stmt|;
comment|// The flag to display a url.
specifier|private
specifier|static
specifier|final
name|String
name|WIN_FLAG
init|=
literal|"url.dll,FileProtocolHandler"
decl_stmt|;
comment|// The default browser under unix.
comment|// private static final String UNIX_PATH = "netscape";
comment|// The flag to display a url.
comment|// private static final String UNIX_FLAG = "-remote openURL";
comment|/**      * Display a file in the system browser. If you want to display a file, you must      * include the absolute path name.      *       * @param url the file's url (the url must start with either "http://" or "file://").      */
comment|// see public domain code at
comment|// http://www.centerkey.com/java/browser/myapp/BareBonesBrowserLaunch.java
specifier|public
specifier|static
name|void
name|displayURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|SystemUtils
operator|.
name|IS_OS_WINDOWS
condition|)
block|{
comment|// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
name|String
name|cmd
init|=
name|WIN_PATH
operator|+
literal|" "
operator|+
name|WIN_FLAG
operator|+
literal|" "
operator|+
name|url
decl_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|SystemUtils
operator|.
name|IS_OS_MAC_OSX
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|fileManager
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.apple.eio.FileManager"
argument_list|)
decl_stmt|;
name|Method
name|openURL
init|=
name|fileManager
operator|.
name|getDeclaredMethod
argument_list|(
literal|"openURL"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|openURL
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
operator|new
name|Object
index|[]
block|{
name|url
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// assume Unix or Linux
name|String
index|[]
name|browsers
init|=
block|{
literal|"firefox"
block|,
literal|"opera"
block|,
literal|"konqueror"
block|,
literal|"epiphany"
block|,
literal|"mozilla"
block|,
literal|"netscape"
block|}
decl_stmt|;
for|for
control|(
name|String
name|browser
range|:
name|browsers
control|)
block|{
if|if
condition|(
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"which"
operator|,
name|browser
block|}
block_content|)
block|.waitFor(
block_content|)
block|== 0
block_content|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
name|browser
block|,
name|url
block|}
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

begin_expr_stmt
unit|}         catch
operator|(
name|Exception
name|ex
operator|)
block|{
comment|// could not open browser. Fail silently.
block|}
end_expr_stmt

unit|} }
end_unit

