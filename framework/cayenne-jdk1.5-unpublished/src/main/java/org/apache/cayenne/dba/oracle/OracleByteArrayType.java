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
name|dba
operator|.
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|Types
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
name|ByteArrayType
import|;
end_import

begin_class
specifier|public
class|class
name|OracleByteArrayType
extends|extends
name|ByteArrayType
block|{
specifier|public
name|OracleByteArrayType
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
if|if
condition|(
name|isUsingBlobs
argument_list|()
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|(
name|byte
index|[]
operator|)
name|val
decl_stmt|;
name|st
operator|.
name|setBinaryStream
argument_list|(
name|pos
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setBytes
argument_list|(
name|pos
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|scale
operator|!=
operator|-
literal|1
condition|)
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

