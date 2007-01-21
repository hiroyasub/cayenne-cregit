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
name|reflect
package|;
end_package

begin_comment
comment|/**  * A convenience base superclass for {@link ToOneProperty} implementors.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseToOneProperty
extends|extends
name|BaseArcProperty
implements|implements
name|ToOneProperty
block|{
specifier|public
name|BaseToOneProperty
parameter_list|(
name|ClassDescriptor
name|owner
parameter_list|,
name|ClassDescriptor
name|targetDescriptor
parameter_list|,
name|Accessor
name|accessor
parameter_list|,
name|String
name|reverseName
parameter_list|)
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setTarget
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
block|{
name|Object
name|oldTarget
init|=
name|readProperty
argument_list|(
name|source
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldTarget
operator|==
name|target
condition|)
block|{
return|return;
block|}
comment|// TODO, Andrus, 2/9/2006 - CayenneDataObject also invokes "willConnect" and has a
comment|// callback to ObjectStore to handle flattened....
if|if
condition|(
name|setReverse
condition|)
block|{
name|setReverse
argument_list|(
name|source
argument_list|,
name|oldTarget
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
name|writeProperty
argument_list|(
name|source
argument_list|,
name|oldTarget
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
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
name|visitToOne
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

