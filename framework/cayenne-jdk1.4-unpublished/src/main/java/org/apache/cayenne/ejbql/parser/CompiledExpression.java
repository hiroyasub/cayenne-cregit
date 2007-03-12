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
name|EJBQLCompiledExpression
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
name|EJBQLExpression
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A compiled EJBQL expression.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|CompiledExpression
implements|implements
name|EJBQLCompiledExpression
block|{
specifier|private
name|String
name|source
decl_stmt|;
specifier|private
name|String
name|rootId
decl_stmt|;
specifier|private
name|Map
name|descriptorsById
decl_stmt|;
specifier|private
name|EJBQLExpression
name|expression
decl_stmt|;
specifier|public
name|ClassDescriptor
name|getEntityDescriptor
parameter_list|(
name|String
name|idVariable
parameter_list|)
block|{
if|if
condition|(
name|idVariable
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// per JPA spec, 4.4.2, "Identification variables are case insensitive."
name|idVariable
operator|=
name|idVariable
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
return|return
operator|(
name|ClassDescriptor
operator|)
name|descriptorsById
operator|.
name|get
argument_list|(
name|idVariable
argument_list|)
return|;
block|}
specifier|public
name|ClassDescriptor
name|getRootDescriptor
parameter_list|()
block|{
return|return
name|rootId
operator|!=
literal|null
condition|?
name|getEntityDescriptor
argument_list|(
name|rootId
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|EJBQLExpression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
name|void
name|setExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
name|void
name|setDescriptorsById
parameter_list|(
name|Map
name|descriptorsById
parameter_list|)
block|{
name|this
operator|.
name|descriptorsById
operator|=
name|descriptorsById
expr_stmt|;
block|}
name|void
name|setSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
name|void
name|setRootId
parameter_list|(
name|String
name|rootId
parameter_list|)
block|{
name|this
operator|.
name|rootId
operator|=
name|rootId
expr_stmt|;
block|}
block|}
end_class

end_unit

