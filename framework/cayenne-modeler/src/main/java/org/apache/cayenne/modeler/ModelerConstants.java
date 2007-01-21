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
name|modeler
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|looks
operator|.
name|plastic
operator|.
name|PlasticXPLookAndFeel
import|;
end_import

begin_comment
comment|/**  * Defines constants used in the modeler.  *   * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|ModelerConstants
block|{
comment|/** Defines path to the images. */
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_PATH
init|=
literal|"org/apache/cayenne/modeler/images/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_MESSAGE_BUNDLE
init|=
literal|"org.apache.cayenne.modeler.cayennemodeler-strings"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TITLE
init|=
literal|"CayenneModeler"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LAF_NAME
init|=
name|PlasticXPLookAndFeel
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// note that previous default - "Desert Blue" theme doesn't support Chinese and
comment|// Japanese chars
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_THEME_NAME
init|=
literal|"Sky Bluer"
decl_stmt|;
block|}
end_interface

end_unit

