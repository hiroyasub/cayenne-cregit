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
name|exp
operator|.
name|property
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|exp
operator|.
name|ExpressionFactory
import|;
end_import

begin_comment
comment|/**  * Property that represents non-numeric PK  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|BaseIdProperty
parameter_list|<
name|E
parameter_list|>
extends|extends
name|BaseProperty
argument_list|<
name|E
argument_list|>
implements|implements
name|IdProperty
argument_list|<
name|E
argument_list|>
block|{
specifier|private
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|private
specifier|final
name|String
name|attributeName
decl_stmt|;
comment|/**      * Constructs a new property with the given name and expression      *      * @param attribute  PK attribute name (optional, can be omitted for single PK entity)      * @param path       cayenne path (optional, can be omitted for  ID of the root)      * @param entityName name of the entity (mandatory)      * @param type       of the property (mandatory)      * @see PropertyFactory#createBaseId(String, String, String, Class)      */
specifier|protected
name|BaseIdProperty
parameter_list|(
name|String
name|attribute
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|entityName
parameter_list|,
name|Class
argument_list|<
name|?
super|super
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|ExpressionFactory
operator|.
name|dbIdPathExp
argument_list|(
name|path
operator|==
literal|null
condition|?
name|attribute
else|:
name|path
operator|+
literal|'.'
operator|+
name|attribute
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
name|this
operator|.
name|attributeName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAttributeName
parameter_list|()
block|{
return|return
name|attributeName
return|;
block|}
block|}
end_class

end_unit

