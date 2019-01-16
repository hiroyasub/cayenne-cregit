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
name|gen
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|property
operator|.
name|NumericProperty
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
name|gen
operator|.
name|PropertyDescriptor
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|NumericPropertyDescriptorCreator
implements|implements
name|PropertyDescriptorCreator
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|PRIMITIVE_NUM_TYPES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|short
operator|.
name|class
argument_list|,
name|long
operator|.
name|class
argument_list|,
name|float
operator|.
name|class
argument_list|,
name|double
operator|.
name|class
argument_list|,
name|byte
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FACTORY_METHOD
init|=
literal|"PropertyFactory.createNumeric"
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|PropertyDescriptor
argument_list|>
name|apply
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
if|if
condition|(
name|PRIMITIVE_NUM_TYPES
operator|.
name|contains
argument_list|(
name|aClass
argument_list|)
operator|||
name|Number
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|aClass
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|PropertyDescriptor
argument_list|(
name|NumericProperty
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FACTORY_METHOD
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

