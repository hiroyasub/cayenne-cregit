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
name|dbsync
operator|.
name|merge
operator|.
name|token
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

begin_comment
comment|/**  * Simple sort of merge tokens.  * Just move all relationships creation tokens to the end of the list.  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|TokenComparator
implements|implements
name|Comparator
argument_list|<
name|MergerToken
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|MergerToken
name|o1
parameter_list|,
name|MergerToken
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
end_class

end_unit

