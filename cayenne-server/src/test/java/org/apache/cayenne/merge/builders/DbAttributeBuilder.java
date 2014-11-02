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
name|merge
operator|.
name|builders
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
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
operator|.
name|isEmpty
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|DbAttributeBuilder
extends|extends
name|DefaultBuilder
argument_list|<
name|DbAttribute
argument_list|>
block|{
specifier|public
name|DbAttributeBuilder
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|DbAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DbAttributeBuilder
name|name
parameter_list|()
block|{
return|return
name|name
argument_list|(
name|getRandomJavaName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|obj
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|type
parameter_list|()
block|{
return|return
name|type
argument_list|(
name|dataFactory
operator|.
name|getItem
argument_list|(
name|TypesMapping
operator|.
name|getDatabaseTypes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|type
parameter_list|(
name|String
name|item
parameter_list|)
block|{
name|obj
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|item
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|typeInt
parameter_list|()
block|{
return|return
name|type
argument_list|(
name|TypesMapping
operator|.
name|SQL_INTEGER
argument_list|)
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|typeVarchar
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|type
argument_list|(
name|TypesMapping
operator|.
name|SQL_VARCHAR
argument_list|)
expr_stmt|;
name|length
argument_list|(
name|length
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|DbAttributeBuilder
name|length
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|obj
operator|.
name|setMaxLength
argument_list|(
name|length
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|primaryKey
parameter_list|()
block|{
name|obj
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|DbAttributeBuilder
name|mandatory
parameter_list|()
block|{
name|obj
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbAttribute
name|build
parameter_list|()
block|{
if|if
condition|(
name|isEmpty
argument_list|(
name|obj
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
name|type
argument_list|()
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbAttribute
name|random
parameter_list|()
block|{
return|return
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

