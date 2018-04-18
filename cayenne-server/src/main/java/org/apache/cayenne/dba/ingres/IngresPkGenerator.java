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
name|ingres
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
name|dba
operator|.
name|JdbcAdapter
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
name|dba
operator|.
name|oracle
operator|.
name|OraclePkGenerator
import|;
end_import

begin_comment
comment|/**  * Ingres-specific sequence based PK generator.  *  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|IngresPkGenerator
extends|extends
name|OraclePkGenerator
block|{
comment|/**      * Used by DI      * @since 4.1      */
specifier|public
name|IngresPkGenerator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|IngresPkGenerator
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|selectNextValQuery
parameter_list|(
name|String
name|sequenceName
parameter_list|)
block|{
return|return
literal|"SELECT "
operator|+
name|sequenceName
operator|+
literal|".nextval"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|selectAllSequencesQuery
parameter_list|()
block|{
return|return
literal|"SELECT seq_name FROM iisequences WHERE seq_owner != 'DBA'"
return|;
block|}
block|}
end_class

end_unit

