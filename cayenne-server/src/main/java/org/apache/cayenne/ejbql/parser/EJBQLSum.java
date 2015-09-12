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
name|ejbql
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|ejbql
operator|.
name|EJBQLExpressionVisitor
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLSum
extends|extends
name|EJBQLAggregateColumn
block|{
comment|// per JPA spec, 4.8.4, SUM type mapping rules are a bit convoluted. Mapping
comment|// them here...
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2256495371122671530L
decl_stmt|;
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeMap
decl_stmt|;
static|static
block|{
name|typeMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|typeMap
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typeMap
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|typeMap
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EJBQLSum
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|visitNode
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitSum
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFunction
parameter_list|()
block|{
return|return
literal|"SUM"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getJavaType
parameter_list|(
name|String
name|pathType
parameter_list|)
block|{
if|if
condition|(
name|pathType
operator|==
literal|null
condition|)
block|{
return|return
literal|"java.lang.Long"
return|;
block|}
comment|// type map only contains mappings that are different from the attribute
comment|// path, so
comment|// if no mapping exists, return the argument passed to this method.
name|String
name|mappedType
init|=
name|typeMap
operator|.
name|get
argument_list|(
name|pathType
argument_list|)
decl_stmt|;
return|return
name|mappedType
operator|!=
literal|null
condition|?
name|mappedType
else|:
name|pathType
return|;
block|}
block|}
end_class

end_unit

