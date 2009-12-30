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

begin_comment
comment|/**  * This is handler for Oracle specific type "oracle.sql.BLOB" Oracle official JDBC  * Driver is mapping SQL BLOB to this type.  */
end_comment

begin_class
specifier|public
class|class
name|OracleBLOBType
extends|extends
name|ByteArrayType
block|{
specifier|public
name|OracleBLOBType
parameter_list|(
name|boolean
name|trimmingBytes
parameter_list|)
block|{
name|super
argument_list|(
name|trimmingBytes
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
literal|"oracle.sql.BLOB"
return|;
block|}
block|}
end_class

end_unit

