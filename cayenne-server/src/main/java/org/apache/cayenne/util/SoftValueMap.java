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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|SoftReference
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

begin_comment
comment|/**  * Map that stores values wrapped into {@link SoftReference}  *  * @see WeakValueMap  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|SoftValueMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ReferenceMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|SoftReference
argument_list|<
name|V
argument_list|>
argument_list|>
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8146103761927411986L
decl_stmt|;
specifier|public
name|SoftValueMap
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SoftValueMap
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
name|super
argument_list|(
name|initialCapacity
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SoftValueMap
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|m
parameter_list|)
block|{
name|super
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|SoftReference
argument_list|<
name|V
argument_list|>
name|newReference
parameter_list|(
name|V
name|value
parameter_list|)
block|{
return|return
operator|new
name|SoftReference
argument_list|<>
argument_list|(
name|value
argument_list|,
name|referenceQueue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

