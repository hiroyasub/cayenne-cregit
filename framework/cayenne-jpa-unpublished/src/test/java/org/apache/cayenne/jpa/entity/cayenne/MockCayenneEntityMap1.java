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
name|jpa
operator|.
name|entity
operator|.
name|cayenne
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedQueries
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|QueryHint
import|;
end_import

begin_class
annotation|@
name|NamedQueries
argument_list|(
block|{
annotation|@
name|NamedQuery
argument_list|(
name|name
operator|=
literal|"query1"
argument_list|,
name|query
operator|=
literal|"select x"
argument_list|,
name|hints
operator|=
block|{
annotation|@
name|QueryHint
argument_list|(
name|name
operator|=
literal|"hint1"
argument_list|,
name|value
operator|=
literal|"value1"
argument_list|)
block|,
annotation|@
name|QueryHint
argument_list|(
name|name
operator|=
literal|"hint2"
argument_list|,
name|value
operator|=
literal|"value2"
argument_list|)
block|}
argument_list|)
block|,
annotation|@
name|NamedQuery
argument_list|(
name|name
operator|=
literal|"query2"
argument_list|,
name|query
operator|=
literal|"select y"
argument_list|)
block|}
argument_list|)
specifier|public
class|class
name|MockCayenneEntityMap1
block|{  }
end_class

end_unit

