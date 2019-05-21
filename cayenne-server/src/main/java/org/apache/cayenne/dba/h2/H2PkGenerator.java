begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|h2
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
comment|/**  * Default PK generator for H2 that uses sequences for PK generation.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|H2PkGenerator
extends|extends
name|OraclePkGenerator
block|{
comment|/**      * Used by DI      * @since 4.1      */
specifier|public
name|H2PkGenerator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|H2PkGenerator
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
name|createSequenceString
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
return|return
literal|"CREATE SEQUENCE "
operator|+
name|sequenceName
argument_list|(
name|ent
argument_list|)
operator|+
literal|" START WITH "
operator|+
name|pkStartValue
operator|+
literal|" INCREMENT BY "
operator|+
name|pkCacheSize
argument_list|(
name|ent
argument_list|)
operator|+
literal|" CACHE 1"
return|;
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
literal|"SELECT NEXT VALUE FOR "
operator|+
name|sequenceName
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
literal|"SELECT LOWER(sequence_name) FROM Information_Schema.Sequences"
return|;
block|}
block|}
end_class

end_unit

