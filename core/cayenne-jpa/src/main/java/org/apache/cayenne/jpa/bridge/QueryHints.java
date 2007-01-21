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
name|jpa
operator|.
name|bridge
package|;
end_package

begin_comment
comment|/**  * Defines QueryHints recognized by Cayenne provider.  *   * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|QueryHints
block|{
specifier|public
specifier|static
specifier|final
name|String
name|QUERY_TYPE_HINT
init|=
literal|"cayenne.query.type"
decl_stmt|;
comment|// must use strings instead of Class.getName() as otherwise Eclipse complains when the
comment|// constant is used in annotations. Is this an Eclipse bug?
specifier|public
specifier|static
specifier|final
name|String
name|SELECT_QUERY
init|=
literal|"org.apache.cayenne.jpa.bridge.JpaSelectQuery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROCEDURE_QUERY
init|=
literal|"org.apache.cayenne.jpa.bridge.JpaProcedureQuery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SQL_TEMPLATE_QUERY
init|=
literal|"org.apache.cayenne.jpa.bridge.JpaSQLTemplate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUALIFIER_HINT
init|=
literal|"cayenne.query.qualifier"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATA_ROWS_HINT
init|=
literal|"cayenne.query.fetchesDataRows"
decl_stmt|;
block|}
end_interface

end_unit

