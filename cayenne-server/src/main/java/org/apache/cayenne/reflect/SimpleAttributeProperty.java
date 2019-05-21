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
name|reflect
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
name|ObjectContext
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
name|map
operator|.
name|ObjAttribute
import|;
end_import

begin_comment
comment|/**  * A descriptor of an "attribute" persistent property.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SimpleAttributeProperty
extends|extends
name|BaseProperty
implements|implements
name|AttributeProperty
block|{
specifier|private
name|ObjAttribute
name|attribute
decl_stmt|;
specifier|public
name|SimpleAttributeProperty
parameter_list|(
name|ClassDescriptor
name|owner
parameter_list|,
name|Accessor
name|accessor
parameter_list|,
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|accessor
argument_list|)
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visit
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitAttribute
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|ObjAttribute
name|getAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readProperty
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
name|resolveFault
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
block|{
name|resolveFault
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|super
operator|.
name|writeProperty
argument_list|(
name|object
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|resolveFault
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
try|try
block|{
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
name|ObjectContext
name|context
init|=
name|persistent
operator|.
name|getObjectContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|prepareForAccess
argument_list|(
name|persistent
argument_list|,
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Object is not a Persistent: '"
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|,
name|this
argument_list|,
name|object
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

