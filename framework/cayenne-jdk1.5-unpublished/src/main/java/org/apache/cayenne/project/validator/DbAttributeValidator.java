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
name|project
operator|.
name|validator
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
name|dba
operator|.
name|TypesMapping
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
name|map
operator|.
name|DbAttribute
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
name|project
operator|.
name|ProjectPath
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DbAttributeValidator
extends|extends
name|TreeNodeValidator
block|{
comment|/**      * Constructor for DbAttributeValidator.      */
specifier|public
name|DbAttributeValidator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Unnamed DbAttribute."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MappingNamesHelper
name|helper
init|=
name|MappingNamesHelper
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|invalidChars
init|=
name|helper
operator|.
name|invalidCharsInDbPathComponent
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalidChars
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"DbAttribute name contains invalid characters: "
operator|+
name|invalidChars
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
comment|// all attributes must have type
if|if
condition|(
name|attribute
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"DbAttribute has no type."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
comment|// VARCHAR and CHAR attributes must have max length
if|else if
condition|(
name|attribute
operator|.
name|getMaxLength
argument_list|()
operator|<
literal|0
operator|&&
operator|(
name|attribute
operator|.
name|getType
argument_list|()
operator|==
name|java
operator|.
name|sql
operator|.
name|Types
operator|.
name|VARCHAR
operator|||
name|attribute
operator|.
name|getType
argument_list|()
operator|==
name|java
operator|.
name|sql
operator|.
name|Types
operator|.
name|CHAR
operator|)
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"Character DbAttribute doesn't have max length."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

