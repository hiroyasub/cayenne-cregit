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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|access
operator|.
name|DataNode
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
name|trans
operator|.
name|QualifierTranslator
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
name|trans
operator|.
name|QueryAssembler
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
name|ExtendedTypeFactory
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
name|configuration
operator|.
name|Constants
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
name|configuration
operator|.
name|RuntimeProperties
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
name|di
operator|.
name|Inject
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|SQLAction
import|;
end_import

begin_comment
comment|/**  * A flavor of OracleAdapter that implements workarounds for some old driver limitations.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|Oracle8Adapter
extends|extends
name|OracleAdapter
block|{
specifier|private
specifier|static
name|Method
name|outputStreamFromBlobMethod
decl_stmt|;
specifier|private
specifier|static
name|Method
name|writerFromClobMethod
decl_stmt|;
static|static
block|{
name|initOracle8DriverInformation
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Oracle8Adapter
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|defaultExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_USER_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|userExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_TYPE_FACTORIES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
parameter_list|)
block|{
name|super
argument_list|(
name|runtimeProperties
argument_list|,
name|defaultExtendedTypes
argument_list|,
name|userExtendedTypes
argument_list|,
name|extendedTypeFactories
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|initOracle8DriverInformation
parameter_list|()
block|{
name|initDone
operator|=
literal|true
expr_stmt|;
comment|// configure static information
try|try
block|{
name|outputStreamFromBlobMethod
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"oracle.sql.BLOB"
argument_list|)
operator|.
name|getMethod
argument_list|(
literal|"getBinaryOutputStream"
argument_list|)
expr_stmt|;
name|writerFromClobMethod
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"oracle.sql.CLOB"
argument_list|)
operator|.
name|getMethod
argument_list|(
literal|"getCharacterOutputStream"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignoring...
block|}
block|}
specifier|static
name|Method
name|getWriterFromClobMethod
parameter_list|()
block|{
return|return
name|writerFromClobMethod
return|;
block|}
specifier|static
name|Method
name|getOutputStreamFromBlobMethod
parameter_list|()
block|{
return|return
name|outputStreamFromBlobMethod
return|;
block|}
comment|/**      * Uses OracleActionBuilder to create the right action.      */
annotation|@
name|Override
specifier|public
name|SQLAction
name|getAction
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
return|return
name|query
operator|.
name|createSQLAction
argument_list|(
operator|new
name|Oracle8ActionBuilder
argument_list|(
name|this
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"/types.xml"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
literal|"/types-oracle8.xml"
expr_stmt|;
block|}
return|return
name|super
operator|.
name|findResource
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|QualifierTranslator
name|getQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|QualifierTranslator
name|translator
init|=
operator|new
name|Oracle8QualifierTranslator
argument_list|(
name|queryAssembler
argument_list|)
decl_stmt|;
name|translator
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitiveCollations
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
block|}
end_class

end_unit

