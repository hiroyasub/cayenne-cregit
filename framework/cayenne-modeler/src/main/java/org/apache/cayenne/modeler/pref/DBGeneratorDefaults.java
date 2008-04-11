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
name|pref
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
name|access
operator|.
name|DbGenerator
import|;
end_import

begin_class
specifier|public
class|class
name|DBGeneratorDefaults
extends|extends
name|_DBGeneratorDefaults
block|{
comment|/**      * Updates DbGenerator settings, consulting its own state.      */
specifier|public
name|void
name|configureGenerator
parameter_list|(
name|DbGenerator
name|generator
parameter_list|)
block|{
name|generator
operator|.
name|setShouldCreateFKConstraints
argument_list|(
name|booleanForBooleanProperty
argument_list|(
name|CREATE_FK_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
name|booleanForBooleanProperty
argument_list|(
name|CREATE_PK_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
name|booleanForBooleanProperty
argument_list|(
name|CREATE_TABLES_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
name|booleanForBooleanProperty
argument_list|(
name|DROP_PK_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
name|booleanForBooleanProperty
argument_list|(
name|DROP_TABLES_PROPERTY
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * An initialization callback.      */
specifier|public
name|void
name|prePersist
parameter_list|()
block|{
name|setCreateFK
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|setCreatePK
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|setCreateTables
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|setDropPK
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|setDropTables
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|booleanForBooleanProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
name|Boolean
name|b
init|=
operator|(
name|Boolean
operator|)
name|readProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
return|return
operator|(
name|b
operator|!=
literal|null
operator|)
condition|?
name|b
operator|.
name|booleanValue
argument_list|()
else|:
literal|false
return|;
block|}
block|}
end_class

end_unit

