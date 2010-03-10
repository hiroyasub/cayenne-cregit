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
name|access
operator|.
name|DbGenerator
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
name|DBGeneratorDefaults
extends|extends
name|RenamedPreferences
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_FK_PROPERTY
init|=
literal|"createFK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_PK_PROPERTY
init|=
literal|"createPK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_TABLES_PROPERTY
init|=
literal|"createTables"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DROP_PK_PROPERTY
init|=
literal|"dropPK"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DROP_TABLES_PROPERTY
init|=
literal|"dropTables"
decl_stmt|;
specifier|public
name|boolean
name|createFK
decl_stmt|;
specifier|public
name|boolean
name|createPK
decl_stmt|;
specifier|public
name|boolean
name|createTables
decl_stmt|;
specifier|public
name|boolean
name|dropPK
decl_stmt|;
specifier|public
name|boolean
name|dropTables
decl_stmt|;
specifier|public
name|DBGeneratorDefaults
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
name|this
operator|.
name|createFK
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|CREATE_FK_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|createPK
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|CREATE_PK_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|createTables
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|CREATE_TABLES_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|dropPK
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|DROP_PK_PROPERTY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|dropTables
operator|=
name|getCurrentPreference
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|DROP_TABLES_PROPERTY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCreateFK
parameter_list|(
name|Boolean
name|createFK
parameter_list|)
block|{
name|this
operator|.
name|createFK
operator|=
name|createFK
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|CREATE_FK_PROPERTY
argument_list|,
name|createFK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getCreateFK
parameter_list|()
block|{
return|return
name|createFK
return|;
block|}
specifier|public
name|void
name|setCreatePK
parameter_list|(
name|Boolean
name|createPK
parameter_list|)
block|{
name|this
operator|.
name|createPK
operator|=
name|createPK
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|CREATE_PK_PROPERTY
argument_list|,
name|createPK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getCreatePK
parameter_list|()
block|{
return|return
name|createPK
return|;
block|}
specifier|public
name|void
name|setCreateTables
parameter_list|(
name|Boolean
name|createTables
parameter_list|)
block|{
name|this
operator|.
name|createTables
operator|=
name|createTables
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|CREATE_TABLES_PROPERTY
argument_list|,
name|createTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getCreateTables
parameter_list|()
block|{
return|return
name|createTables
return|;
block|}
specifier|public
name|void
name|setDropPK
parameter_list|(
name|Boolean
name|dropPK
parameter_list|)
block|{
name|this
operator|.
name|dropPK
operator|=
name|dropPK
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|DROP_PK_PROPERTY
argument_list|,
name|dropPK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getDropPK
parameter_list|()
block|{
return|return
name|dropPK
return|;
block|}
specifier|public
name|void
name|setDropTables
parameter_list|(
name|Boolean
name|dropTables
parameter_list|)
block|{
name|this
operator|.
name|dropTables
operator|=
name|dropTables
expr_stmt|;
name|getCurrentPreference
argument_list|()
operator|.
name|putBoolean
argument_list|(
name|DROP_TABLES_PROPERTY
argument_list|,
name|dropTables
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getDropTables
parameter_list|()
block|{
return|return
name|dropTables
return|;
block|}
comment|/**      * Updates DbGenerator settings, consulting its own state.      */
specifier|public
name|void
name|configureGenerator
parameter_list|(
name|DbGenerator
name|generator
parameter_list|)
block|{
name|setCreateFK
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
name|setCreatePK
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
name|setCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
name|setDropPK
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
name|setDropTables
argument_list|(
name|dropTables
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateFKConstraints
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
name|dropTables
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
block|}
end_class

end_unit

