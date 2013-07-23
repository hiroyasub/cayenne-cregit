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
name|testdo
operator|.
name|testmap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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

begin_class
specifier|public
class|class
name|StringET1ExtendedType
implements|implements
name|ExtendedType
block|{
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|StringET1
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|string
operator|!=
literal|null
condition|?
operator|new
name|StringET1
argument_list|(
name|string
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|string
operator|!=
literal|null
condition|?
operator|new
name|StringET1
argument_list|(
name|string
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|instanceof
name|StringET1
condition|)
block|{
name|statement
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
operator|(
name|StringET1
operator|)
name|value
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
