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
name|io
operator|.
name|IOException
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
comment|// Used to identify the windows platform.
specifier|private
specifier|static
specifier|final
name|String
name|WIN_ID
init|=
literal|"Windows"
decl_stmt|;
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
specifier|public
specifier|static
name|void
name|displayURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|boolean
name|windows
init|=
name|isWindowsPlatform
argument_list|()
decl_stmt|;
name|String
name|cmd
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|windows
condition|)
block|{
comment|// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
name|cmd
operator|=
name|WIN_PATH
operator|+
literal|" "
operator|+
name|WIN_FLAG
operator|+
literal|" "
operator|+
name|url
expr_stmt|;
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
else|else
block|{
comment|// unsupported...
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Try to determine whether this application is running under Windows or some other      * platform by examing the "os.name" property.      *       * @return true if this application is running under a Windows OS      */
specifier|public
specifier|static
name|boolean
name|isWindowsPlatform
parameter_list|()
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|!=
literal|null
operator|&&
name|os
operator|.
name|startsWith
argument_list|(
name|WIN_ID
argument_list|)
condition|)
return|return
literal|true
return|;
else|else
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

