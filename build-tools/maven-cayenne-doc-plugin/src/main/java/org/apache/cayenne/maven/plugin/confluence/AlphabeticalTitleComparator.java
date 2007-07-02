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
name|maven
operator|.
name|plugin
operator|.
name|confluence
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

begin_class
class|class
name|AlphabeticalTitleComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
name|DocPage
name|child0
init|=
operator|(
name|DocPage
operator|)
name|arg0
decl_stmt|;
name|DocPage
name|child1
init|=
operator|(
name|DocPage
operator|)
name|arg1
decl_stmt|;
return|return
operator|(
name|child0
operator|.
name|getTitle
argument_list|()
operator|.
name|compareTo
argument_list|(
name|child1
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

