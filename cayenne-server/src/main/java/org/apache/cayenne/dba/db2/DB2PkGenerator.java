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
name|db2
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
comment|/**  * A sequence-based PK generator used by {@link DB2Adapter}.  */
end_comment

begin_class
specifier|public
class|class
name|DB2PkGenerator
extends|extends
name|OraclePkGenerator
block|{
comment|/**      * Used by DI      * @since 4.1      */
specifier|public
name|DB2PkGenerator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
name|DB2PkGenerator
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
specifier|private
specifier|static
specifier|final
name|String
name|_SEQUENCE_PREFIX
init|=
literal|"S_"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
name|sequenceName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|super
operator|.
name|sequenceName
argument_list|(
name|entity
argument_list|)
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getSequencePrefix
parameter_list|()
block|{
return|return
name|_SEQUENCE_PREFIX
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|selectNextValQuery
parameter_list|(
name|String
name|pkGeneratingSequenceName
parameter_list|)
block|{
return|return
literal|"SELECT NEXTVAL FOR "
operator|+
name|pkGeneratingSequenceName
operator|+
literal|" FROM SYSIBM.SYSDUMMY1"
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
literal|"SELECT SEQNAME FROM SYSCAT.SEQUENCES WHERE SEQNAME LIKE '"
operator|+
name|_SEQUENCE_PREFIX
operator|+
literal|"%'"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|dropSequenceString
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
literal|"DROP SEQUENCE "
operator|+
name|sequenceName
argument_list|(
name|entity
argument_list|)
operator|+
literal|" RESTRICT "
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|createSequenceString
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
literal|"CREATE SEQUENCE "
operator|+
name|sequenceName
argument_list|(
name|entity
argument_list|)
operator|+
literal|" AS BIGINT START WITH "
operator|+
name|pkStartValue
operator|+
literal|" INCREMENT BY "
operator|+
name|getPkCacheSize
argument_list|()
operator|+
literal|" NO MAXVALUE NO CYCLE CACHE "
operator|+
name|getPkCacheSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

