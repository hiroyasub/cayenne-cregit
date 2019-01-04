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
name|Set
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
name|Persistent
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
name|Expression
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
name|parser
operator|.
name|ASTPath
import|;
end_import

begin_comment
comment|/**  * Property that represents to-many relationship mapped on {@link Set}.  *  * @see org.apache.cayenne.exp.property  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SetProperty
parameter_list|<
name|V
extends|extends
name|Persistent
parameter_list|>
extends|extends
name|CollectionProperty
argument_list|<
name|V
argument_list|,
name|Set
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
comment|/**      * Constructs a new property with the given name and expression      *      * @param name           of the property (will be used as alias for the expression)      * @param expression     expression for property      * @param entityType     type of related entity      */
specifier|protected
name|SetProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|entityType
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|Set
operator|.
name|class
argument_list|,
name|entityType
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|SetProperty
argument_list|<
name|V
argument_list|>
name|alias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|ASTPath
name|exp
init|=
name|PropertyUtils
operator|.
name|createPathExp
argument_list|(
name|this
operator|.
name|getName
argument_list|()
argument_list|,
name|alias
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createSet
argument_list|(
name|exp
operator|.
name|getPath
argument_list|()
argument_list|,
name|exp
argument_list|,
name|this
operator|.
name|getEntityType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|SetProperty
argument_list|<
name|V
argument_list|>
name|outer
parameter_list|()
block|{
return|return
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"+"
argument_list|)
condition|?
name|this
else|:
name|PropertyFactory
operator|.
name|createSet
argument_list|(
name|getName
argument_list|()
operator|+
literal|"+"
argument_list|,
name|getEntityType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

