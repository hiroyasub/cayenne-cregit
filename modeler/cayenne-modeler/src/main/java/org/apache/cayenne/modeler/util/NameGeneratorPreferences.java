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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|dbsync
operator|.
name|naming
operator|.
name|NoStemStemmer
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
name|dbsync
operator|.
name|naming
operator|.
name|ObjectNameGenerator
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
name|Application
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

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

begin_comment
comment|/**  * Helper class to store/read information about naming strategies have been used  */
end_comment

begin_class
specifier|public
class|class
name|NameGeneratorPreferences
block|{
specifier|private
specifier|static
specifier|final
name|String
name|STRATEGIES_PREFERENCE
init|=
literal|"name.generators.recent"
decl_stmt|;
comment|/**      * Naming strategies to appear in combobox by default      */
specifier|private
specifier|static
specifier|final
name|Vector
argument_list|<
name|String
argument_list|>
name|PREDEFINED_STRATEGIES
init|=
operator|new
name|Vector
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|PREDEFINED_STRATEGIES
operator|.
name|add
argument_list|(
name|DefaultObjectNameGenerator
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|NameGeneratorPreferences
name|instance
init|=
operator|new
name|NameGeneratorPreferences
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|NameGeneratorPreferences
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
name|Preferences
name|getPreference
parameter_list|()
block|{
return|return
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getMainPreferenceForProject
argument_list|()
return|;
block|}
comment|/**      * @return last used strategies, PREDEFINED_STRATEGIES by default      */
specifier|public
name|Vector
argument_list|<
name|String
argument_list|>
name|getLastUsedStrategies
parameter_list|()
block|{
name|String
name|prop
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getPreference
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|prop
operator|=
name|getPreference
argument_list|()
operator|.
name|get
argument_list|(
name|STRATEGIES_PREFERENCE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
return|return
name|PREDEFINED_STRATEGIES
return|;
block|}
return|return
operator|new
name|Vector
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|prop
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds strategy to history      */
specifier|public
name|void
name|addToLastUsedStrategies
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
name|Vector
argument_list|<
name|String
argument_list|>
name|strategies
init|=
name|getLastUsedStrategies
argument_list|()
decl_stmt|;
comment|// move to top
name|strategies
operator|.
name|remove
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|strategies
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|str
range|:
name|strategies
control|)
block|{
name|res
operator|.
name|append
argument_list|(
name|str
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|strategies
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|res
operator|.
name|deleteCharAt
argument_list|(
name|res
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|getPreference
argument_list|()
operator|.
name|put
argument_list|(
name|STRATEGIES_PREFERENCE
argument_list|,
name|res
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjectNameGenerator
name|createNamingStrategy
parameter_list|(
name|Application
name|application
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
return|return
name|application
operator|.
name|getClassLoadingService
argument_list|()
operator|.
name|loadClass
argument_list|(
name|ObjectNameGenerator
operator|.
name|class
argument_list|,
name|getLastUsedStrategies
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ObjectNameGenerator
name|defaultNameGenerator
parameter_list|()
block|{
return|return
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

