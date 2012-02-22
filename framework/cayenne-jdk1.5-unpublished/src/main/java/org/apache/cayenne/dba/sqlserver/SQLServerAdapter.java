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
name|sqlserver
package|;
end_package

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
name|dba
operator|.
name|sybase
operator|.
name|SybaseAdapter
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
name|merge
operator|.
name|MergerFactory
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
comment|/**  * Cayenne DbAdapter implementation for<a  * href="http://www.microsoft.com/sql/"Microsoft SQL Server</a> engine.  *</p>  *<h3>Microsoft Driver Settings</h3>  *<p>  * Sample connection settings to use with MS SQL Server are shown below:  *   *<pre>  *       sqlserver.cayenne.adapter = org.apache.cayenne.dba.sqlserver.SQLServerAdapter  *       sqlserver.jdbc.username = test  *       sqlserver.jdbc.password = secret  *       sqlserver.jdbc.url = jdbc:sqlserver://192.168.0.65;databaseName=cayenne;SelectMethod=cursor  *       sqlserver.jdbc.driver = com.microsoft.sqlserver.jdbc.SQLServerDriver  *</pre>  *   *<p>  *<i>Note on case-sensitive LIKE: if your application requires case-sensitive LIKE  * support, ask your DBA to configure the database to use a case-senstitive collation (one  * with "CS" in symbolic collation name instead of "CI", e.g.  * "SQL_Latin1_general_CP1_CS_AS").</i>  *</p>  *<h3>jTDS Driver Settings</h3>  *<p>  * jTDS is an open source driver that can be downloaded from<a href=  * "http://jtds.sourceforge.net">http://jtds.sourceforge.net</a>. It supports both  * SQLServer and Sybase. Sample SQLServer settings are the following:  *</p>  *   *<pre>  *   *    *     *       sqlserver.cayenne.adapter = org.apache.cayenne.dba.sqlserver.SQLServerAdapter  *       sqlserver.jdbc.username = test  *       sqlserver.jdbc.password = secret  *       sqlserver.jdbc.url = jdbc:jtds:sqlserver://192.168.0.65/cayenne  *       sqlserver.jdbc.driver = net.sourceforge.jtds.jdbc.Driver  *      *     *    *</pre>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerAdapter
extends|extends
name|SybaseAdapter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TRIM_FUNCTION
init|=
literal|"RTRIM"
decl_stmt|;
specifier|public
name|SQLServerAdapter
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|DEFAULT_EXTENDED_TYPE_LIST
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
name|USER_EXTENDED_TYPE_LIST
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
name|EXTENDED_TYPE_FACTORY_LIST
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
comment|// TODO: i wonder if Sybase supports generated keys...
comment|// in this case we need to move this to the super.
name|this
operator|.
name|setSupportsGeneratedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Uses SQLServerActionBuilder to create the right action.      *       * @since 1.2      */
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
name|SQLServerActionBuilder
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
comment|/**      * Returns a trimming translator.      */
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
name|SQLServerTrimmingQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|,
name|SQLServerAdapter
operator|.
name|TRIM_FUNCTION
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
comment|/**      * Overrides super implementation to correctly set up identity columns.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|void
name|createTableAppendColumn
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
if|if
condition|(
name|column
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
comment|// current limitation - we don't allow to set identity parameters...
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" IDENTITY (1, 1)"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|SQLServerMergerFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

