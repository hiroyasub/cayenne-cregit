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
name|swing
operator|.
name|control
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
name|javax
operator|.
name|swing
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
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
name|modeler
operator|.
name|util
operator|.
name|CayenneAction
import|;
end_import

begin_comment
comment|/**  * A menu item that points to a file.  *   */
end_comment

begin_class
specifier|public
class|class
name|FileMenuItem
extends|extends
name|CayenneAction
operator|.
name|CayenneMenuItem
block|{
comment|/**      * Creates a new instance with the specified fileName.      */
specifier|public
name|FileMenuItem
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|super
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|configurePropertiesFromAction
parameter_list|(
name|Action
name|a
parameter_list|)
block|{
comment|// excludes most generic action keys that are not applicable here...
name|setIcon
argument_list|(
name|a
operator|!=
literal|null
condition|?
operator|(
name|Icon
operator|)
name|a
operator|.
name|getValue
argument_list|(
name|Action
operator|.
name|SMALL_ICON
argument_list|)
else|:
literal|null
argument_list|)
expr_stmt|;
name|setEnabled
argument_list|(
name|a
operator|==
literal|null
operator|||
name|a
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|updateActiveIcon
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns a file if this menu item points to a readable file or directory, or null      * otherwise.      */
specifier|public
name|File
name|getFile
parameter_list|()
block|{
if|if
condition|(
name|getText
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|getText
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|f
operator|.
name|canRead
argument_list|()
condition|?
name|f
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

