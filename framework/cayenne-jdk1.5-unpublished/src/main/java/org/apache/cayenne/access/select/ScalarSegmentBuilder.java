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
name|access
operator|.
name|select
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
operator|.
name|ExtendedType
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
name|access
operator|.
name|types
operator|.
name|ExtendedTypeMap
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
name|query
operator|.
name|ScalarResultSegment
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|ScalarSegmentBuilder
block|{
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|resultDescriptors
decl_stmt|;
specifier|private
name|ExtendedType
name|converter
decl_stmt|;
name|ScalarSegmentBuilder
parameter_list|(
name|ExtendedTypeMap
name|extendedTypes
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|resultDescriptors
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|extendedTypes
operator|.
name|getDefaultType
argument_list|()
expr_stmt|;
name|this
operator|.
name|resultDescriptors
operator|=
name|resultDescriptors
expr_stmt|;
block|}
name|SelectDescriptor
argument_list|<
name|Object
argument_list|>
name|getSegment
parameter_list|(
name|int
name|position
parameter_list|)
block|{
name|ScalarResultSegment
name|segment
init|=
operator|(
name|ScalarResultSegment
operator|)
name|resultDescriptors
operator|.
name|get
argument_list|(
name|position
argument_list|)
decl_stmt|;
return|return
operator|new
name|ScalarSegment
argument_list|(
name|segment
operator|.
name|getColumn
argument_list|()
argument_list|,
name|converter
argument_list|)
return|;
block|}
block|}
end_class

end_unit

