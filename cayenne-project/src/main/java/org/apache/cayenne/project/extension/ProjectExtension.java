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
name|project
operator|.
name|extension
package|;
end_package

begin_comment
comment|/**  *<p>DataMap XML file extension mechanics.</p>  *<p>  *     Can be used to enhance datamap.map.xml files with additional (really random) information.  *     By default extensions not used by {@link org.apache.cayenne.configuration.server.ServerRuntime} or  *     ClientRuntime so they can safely store big chunks of data.  *</p>  *<p>  *     Extensions can be contributed by {@link org.apache.cayenne.project.ProjectModule#contributeExtensions(org.apache.cayenne.di.Binder)}.  *     {@link org.apache.cayenne.project.ProjectModule} currently used by Modeler and cli tools, e.g. cdbimport and cgen.  *</p>  *  * @since 4.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProjectExtension
block|{
comment|/**      * @return delegate that handle loading phase of XML processing      */
name|LoaderDelegate
name|createLoaderDelegate
parameter_list|()
function_decl|;
comment|/**      * @return delegate that handle saving phase of XML processing      */
name|SaverDelegate
name|createSaverDelegate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

