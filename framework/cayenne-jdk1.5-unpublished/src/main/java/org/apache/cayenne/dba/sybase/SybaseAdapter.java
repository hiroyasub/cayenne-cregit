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
name|SQLException
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
name|jdbc
operator|.
name|EJBQLTranslatorFactory
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
name|ByteType
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
name|CharType
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
name|ExtendedTypeMap
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
name|ShortType
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
name|PkGenerator
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

begin_comment
comment|/**   * DbAdapter implementation for<a href="http://www.sybase.com">Sybase RDBMS</a>.  *  */
end_comment

begin_class
specifier|public
class|class
name|SybaseAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|final
specifier|static
name|String
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_START
init|=
literal|"["
decl_stmt|;
specifier|final
specifier|static
name|String
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_END
init|=
literal|"]"
decl_stmt|;
comment|/**     *      * @since 3.0     */
annotation|@
name|Override
specifier|public
name|void
name|initIdentifiersQuotes
parameter_list|()
block|{
name|this
operator|.
name|identifiersStartQuote
operator|=
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_START
expr_stmt|;
name|this
operator|.
name|identifiersEndQuote
operator|=
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_END
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|protected
name|EJBQLTranslatorFactory
name|createEJBQLTranslatorFactory
parameter_list|()
block|{
return|return
operator|new
name|SybaseEJBQLTranslatorFactory
argument_list|()
return|;
block|}
comment|/**      * Returns word "go".      *       * @since 1.0.4      */
annotation|@
name|Override
specifier|public
name|String
name|getBatchTerminator
parameter_list|()
block|{
return|return
literal|"go"
return|;
block|}
comment|/**      * Installs appropriate ExtendedTypes as converters for passing values      * between JDBC and Java layers.      */
annotation|@
name|Override
specifier|protected
name|void
name|configureExtendedTypes
parameter_list|(
name|ExtendedTypeMap
name|map
parameter_list|)
block|{
name|super
operator|.
name|configureExtendedTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// create specially configured CharType handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CharType
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// create specially configured ByteArrayType handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ByteArrayType
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// address Sybase driver inability to handle java.lang.Short and java.lang.Byte
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ShortType
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ByteType
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**       * Creates and returns a primary key generator.       * Overrides superclass implementation to return an      * instance of SybasePkGenerator.      */
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|SybasePkGenerator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameter
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|object
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
comment|// Sybase driver doesn't like CLOBs and BLOBs as parameters
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|sqlType
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|sqlType
operator|=
name|Types
operator|.
name|VARCHAR
expr_stmt|;
block|}
if|else if
condition|(
name|sqlType
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|sqlType
operator|=
name|Types
operator|.
name|VARBINARY
expr_stmt|;
block|}
block|}
if|if
condition|(
name|object
operator|==
literal|null
operator|&&
name|sqlType
operator|==
literal|0
condition|)
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|object
argument_list|,
name|pos
argument_list|,
name|sqlType
argument_list|,
name|precision
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
name|SybaseMergerFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

