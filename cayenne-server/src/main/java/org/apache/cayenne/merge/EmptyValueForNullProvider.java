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
name|merge
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
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
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * A dummy {@link ValueForNullProvider} that are not able to provide any values  */
end_comment

begin_class
class|class
name|EmptyValueForNullProvider
implements|implements
name|ValueForNullProvider
block|{
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasValueFor
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

