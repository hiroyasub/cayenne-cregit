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
operator|.
name|generic
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
name|DataObject
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
name|PersistentDescriptor
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
name|PropertyException
import|;
end_import

begin_comment
comment|/**  * A ClassDescriptor for "generic" persistent classes implementing {@link DataObject}  * interface.  *   * @since 3.0  */
end_comment

begin_comment
comment|// non-public as the only difference with the superclass is version handling on merge -
end_comment

begin_comment
comment|// this is something we need to solved in a more generic fashion (e.g. as via enhancer)
end_comment

begin_comment
comment|// for other object types.
end_comment

begin_class
class|class
name|DataObjectDescriptor
extends|extends
name|PersistentDescriptor
block|{
annotation|@
name|Override
specifier|public
name|void
name|shallowMerge
parameter_list|(
name|Object
name|from
parameter_list|,
name|Object
name|to
parameter_list|)
throws|throws
name|PropertyException
block|{
name|injectValueHolders
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|super
operator|.
name|shallowMerge
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
name|from
operator|instanceof
name|DataObject
operator|&&
name|to
operator|instanceof
name|DataObject
condition|)
block|{
operator|(
operator|(
name|DataObject
operator|)
name|to
operator|)
operator|.
name|setSnapshotVersion
argument_list|(
operator|(
operator|(
name|DataObject
operator|)
name|from
operator|)
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

