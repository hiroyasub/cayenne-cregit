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
name|project
operator|.
name|upgrade
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|ConfigurationException
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
name|QueryDescriptor
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
name|project
operator|.
name|upgrade
operator|.
name|UpgradeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeHandler_V8
implements|implements
name|UpgradeHandler
block|{
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
literal|"8"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processProjectDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|Element
name|domain
init|=
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|domain
operator|.
name|setAttribute
argument_list|(
literal|"project-version"
argument_list|,
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processDataMapDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|updateDataMapSchemaAndVersion
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|NodeList
name|queryNodes
decl_stmt|;
try|try
block|{
name|queryNodes
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/query"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|queryNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|queryElement
init|=
operator|(
name|Element
operator|)
name|queryNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|factory
init|=
name|queryElement
operator|.
name|getAttribute
argument_list|(
literal|"factory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
operator|||
name|factory
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|String
name|queryType
decl_stmt|;
switch|switch
condition|(
name|factory
condition|)
block|{
case|case
literal|"org.apache.cayenne.map.SelectQueryBuilder"
case|:
name|queryType
operator|=
name|QueryDescriptor
operator|.
name|SELECT_QUERY
expr_stmt|;
break|break;
case|case
literal|"org.apache.cayenne.map.SQLTemplateBuilder"
case|:
name|queryType
operator|=
name|QueryDescriptor
operator|.
name|SQL_TEMPLATE
expr_stmt|;
break|break;
case|case
literal|"org.apache.cayenne.map.EjbqlBuilder"
case|:
name|queryType
operator|=
name|QueryDescriptor
operator|.
name|EJBQL_QUERY
expr_stmt|;
break|break;
case|case
literal|"org.apache.cayenne.map.ProcedureQueryBuilder"
case|:
name|queryType
operator|=
name|QueryDescriptor
operator|.
name|PROCEDURE_QUERY
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Unknown query factory: "
operator|+
name|factory
argument_list|)
throw|;
block|}
name|queryElement
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
name|queryType
argument_list|)
expr_stmt|;
name|queryElement
operator|.
name|removeAttribute
argument_list|(
literal|"factory"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

