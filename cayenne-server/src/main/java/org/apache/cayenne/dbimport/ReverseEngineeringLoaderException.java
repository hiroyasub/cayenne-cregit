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
name|dbimport
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
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_comment
comment|/**  * @since 4.0  * @deprecated  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringLoaderException
extends|extends
name|CayenneRuntimeException
block|{
specifier|public
name|ReverseEngineeringLoaderException
parameter_list|(
name|String
name|message
parameter_list|,
name|ParserConfigurationException
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReverseEngineeringLoaderException
parameter_list|(
name|String
name|message
parameter_list|,
name|SAXException
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

