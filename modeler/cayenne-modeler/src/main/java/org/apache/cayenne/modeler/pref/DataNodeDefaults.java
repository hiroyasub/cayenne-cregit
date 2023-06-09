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
name|modeler
operator|.
name|pref
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|Preferences
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
name|pref
operator|.
name|RenamedPreferences
import|;
end_import

begin_class
specifier|public
class|class
name|DataNodeDefaults
extends|extends
name|RenamedPreferences
block|{
specifier|private
name|String
name|localDataSource
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOCAL_DATA_SOURCE_PROPERTY
init|=
literal|"localDataSource"
decl_stmt|;
specifier|public
name|DataNodeDefaults
parameter_list|(
name|Preferences
name|pref
parameter_list|)
block|{
name|super
argument_list|(
name|pref
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLocalDataSource
parameter_list|(
name|String
name|localDataSource
parameter_list|)
block|{
if|if
condition|(
name|getCurrentPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|localDataSource
operator|=
name|localDataSource
expr_stmt|;
if|if
condition|(
name|localDataSource
operator|!=
literal|null
condition|)
block|{
name|getCurrentPreference
argument_list|()
operator|.
name|put
argument_list|(
name|LOCAL_DATA_SOURCE_PROPERTY
argument_list|,
name|localDataSource
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getLocalDataSource
parameter_list|()
block|{
if|if
condition|(
name|localDataSource
operator|==
literal|null
condition|)
block|{
name|localDataSource
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|get
argument_list|(
name|LOCAL_DATA_SOURCE_PROPERTY
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
return|return
name|localDataSource
return|;
block|}
block|}
end_class

end_unit

