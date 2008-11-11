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
name|unit
operator|.
name|util
package|;
end_package

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
name|dba
operator|.
name|DbAdapter
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
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_comment
comment|/**  * Helper class to customize SQLTemplate queries used in test cases per adapter.  *   */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateCustomizer
block|{
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|Map
name|sqlMap
decl_stmt|;
specifier|public
name|SQLTemplateCustomizer
parameter_list|(
name|Map
name|sqlMap
parameter_list|)
block|{
name|this
operator|.
name|sqlMap
operator|=
name|sqlMap
expr_stmt|;
block|}
comment|/**      * Customizes SQLTemplate, injecting the template for the current adapter.      */
specifier|public
name|void
name|updateSQLTemplate
parameter_list|(
name|SQLTemplate
name|query
parameter_list|)
block|{
name|Map
name|customSQL
init|=
operator|(
name|Map
operator|)
name|sqlMap
operator|.
name|get
argument_list|(
name|query
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|customSQL
operator|!=
literal|null
condition|)
block|{
name|String
name|key
init|=
name|adapter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|template
init|=
operator|(
name|String
operator|)
name|customSQL
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setTemplate
argument_list|(
name|key
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|SQLTemplate
name|createSQLTemplate
parameter_list|(
name|Class
name|root
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|root
argument_list|,
name|defaultTemplate
argument_list|)
decl_stmt|;
name|updateSQLTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
return|return
name|template
return|;
block|}
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
block|}
end_class

end_unit

