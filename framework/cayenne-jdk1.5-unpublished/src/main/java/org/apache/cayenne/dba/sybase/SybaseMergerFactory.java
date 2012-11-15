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
name|sybase
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
name|dba
operator|.
name|DbAdapter
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
name|QuotingStrategy
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
name|AddColumnToDb
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
name|DropColumnToDb
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
name|merge
operator|.
name|MergerToken
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
name|SetAllowNullToDb
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
name|SetColumnTypeToDb
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
name|SetNotNullToDb
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SybaseMergerFactory
extends|extends
name|MergerFactory
block|{
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|MergerToken
name|createAddColumnToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
specifier|final
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|AddColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|StringBuffer
name|sqlBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" ADD "
argument_list|)
expr_stmt|;
name|boolean
name|magnatory
init|=
name|column
operator|.
name|isMandatory
argument_list|()
decl_stmt|;
name|column
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|adapter
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
name|magnatory
condition|)
block|{
name|column
operator|.
name|setMandatory
argument_list|(
name|magnatory
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|MergerToken
name|createDropColumnToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|DropColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|StringBuilder
name|sqlBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" DROP "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedIdentifier
argument_list|(
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|MergerToken
name|createSetNotNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetNotNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|StringBuffer
name|sqlBuffer
init|=
name|createStringQuery
argument_list|(
name|adapter
argument_list|,
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|MergerToken
name|createSetAllowNullToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|SetAllowNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|StringBuffer
name|sqlBuffer
init|=
name|createStringQuery
argument_list|(
name|adapter
argument_list|,
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|MergerToken
name|createSetColumnTypeToDb
parameter_list|(
specifier|final
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|columnOriginal
parameter_list|,
specifier|final
name|DbAttribute
name|columnNew
parameter_list|)
block|{
return|return
operator|new
name|SetColumnTypeToDb
argument_list|(
name|entity
argument_list|,
name|columnOriginal
argument_list|,
name|columnNew
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|appendPrefix
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|QuotingStrategy
name|context
parameter_list|)
block|{
comment|// http://dev.mysql.com/tech-resources/articles/mysql-cluster-50.html
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" MODIFY "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedIdentifier
argument_list|(
name|columnNew
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
specifier|private
specifier|static
name|StringBuffer
name|createStringQuery
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|StringBuffer
name|sqlBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|(
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" MODIFY "
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
return|return
name|sqlBuffer
return|;
block|}
block|}
end_class

end_unit

