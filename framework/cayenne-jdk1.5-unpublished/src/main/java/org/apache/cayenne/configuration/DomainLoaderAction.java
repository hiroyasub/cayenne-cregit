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
name|configuration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|DataDomain
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
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|DomainLoaderAction
extends|extends
name|DefaultHandler
block|{
name|DataDomain
name|loadDomain
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
comment|// try {
comment|// XMLReader parser = Util.createXmlReader();
comment|//
comment|// parser.setContentHandler(this);
comment|// parser.setErrorHandler(this);
comment|// parser.parse(new InputSource(in));
comment|// }
comment|// catch (Exception ex) {
comment|//
comment|// }
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

