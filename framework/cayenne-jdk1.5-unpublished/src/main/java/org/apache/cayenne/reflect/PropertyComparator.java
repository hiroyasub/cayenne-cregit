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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|final
class|class
name|PropertyComparator
implements|implements
name|Comparator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|PropertyDescriptor
argument_list|>
argument_list|>
block|{
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|PropertyDescriptor
argument_list|>
argument_list|>
name|comparator
init|=
operator|new
name|PropertyComparator
argument_list|()
decl_stmt|;
specifier|public
name|int
name|compare
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|PropertyDescriptor
argument_list|>
name|o1
parameter_list|,
name|Entry
argument_list|<
name|String
argument_list|,
name|PropertyDescriptor
argument_list|>
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|.
name|getValue
argument_list|()
operator|instanceof
name|ArcProperty
condition|)
block|{
if|if
condition|(
name|o2
operator|.
name|getValue
argument_list|()
operator|instanceof
name|ArcProperty
condition|)
block|{
return|return
name|o1
operator|.
name|getKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|1
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|o2
operator|.
name|getValue
argument_list|()
operator|instanceof
name|ArcProperty
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
name|o1
operator|.
name|getKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

