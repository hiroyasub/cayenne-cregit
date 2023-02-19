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
name|value
operator|.
name|json
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
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataContext
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
name|SelectById
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
name|testdo
operator|.
name|json
operator|.
name|JsonOther
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
name|testdo
operator|.
name|json
operator|.
name|JsonVarchar
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
name|unit
operator|.
name|OracleUnitDbAdapter
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
name|unit
operator|.
name|UnitDbAdapter
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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
name|value
operator|.
name|Json
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|JSON_PROJECT
argument_list|)
specifier|public
class|class
name|JsonTypeIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|unitDbAdapter
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testJsonBasic
parameter_list|()
block|{
name|testJson
argument_list|(
literal|"{\"id\": 1, \"property\": \"value\"}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonOver4k
parameter_list|()
block|{
name|testJson
argument_list|(
literal|"[\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f151cd2df4280fe4258ef3\",\n"
operator|+
literal|"    \"index\": 0,\n"
operator|+
literal|"    \"guid\": \"2e05e933-7468-4573-aa76-e29a80810c57\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$3,282.00\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 20,\n"
operator|+
literal|"    \"eyeColor\": \"blue\",\n"
operator|+
literal|"    \"name\": \"Mathews Rutledge\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"FREAKIN\",\n"
operator|+
literal|"    \"email\": \"mathewsrutledge@freakin.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (873) 411-3555\",\n"
operator|+
literal|"    \"address\": \"896 Lee Avenue, Fairhaven, New Jersey, 1031\",\n"
operator|+
literal|"    \"about\": \"nostrud cupidatat proident eu anim sint eu\",\n"
operator|+
literal|"    \"registered\": \"2021-10-10T11:59:50 -03:00\",\n"
operator|+
literal|"    \"latitude\": -18.854904,\n"
operator|+
literal|"    \"longitude\": -138.392089,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"ad\",\n"
operator|+
literal|"      \"irure\",\n"
operator|+
literal|"      \"anim\",\n"
operator|+
literal|"      \"aliqua\",\n"
operator|+
literal|"      \"do\",\n"
operator|+
literal|"      \"eu\",\n"
operator|+
literal|"      \"excepteur\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Guzman Kemp\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Delgado Beasley\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Noelle Owen\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Mathews Rutledge! You have 4 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f151cd00a687261825c3d3\",\n"
operator|+
literal|"    \"index\": 1,\n"
operator|+
literal|"    \"guid\": \"7c6b8859-5a81-4654-980f-e31ae7c3a04d\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,805.03\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 32,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Nieves Gallegos\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"SPACEWAX\",\n"
operator|+
literal|"    \"email\": \"nievesgallegos@spacewax.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (839) 414-3310\",\n"
operator|+
literal|"    \"address\": \"446 Mill Avenue, Nicholson, Minnesota, 8852\",\n"
operator|+
literal|"    \"about\": \"sunt magna quis officia exercitation laboris officia\",\n"
operator|+
literal|"    \"registered\": \"2020-01-17T08:49:38 -03:00\",\n"
operator|+
literal|"    \"latitude\": 11.681513,\n"
operator|+
literal|"    \"longitude\": -129.960233,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"labore\",\n"
operator|+
literal|"      \"fugiat\",\n"
operator|+
literal|"      \"cillum\",\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"nostrud\",\n"
operator|+
literal|"      \"non\",\n"
operator|+
literal|"      \"et\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Perry Hunter\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Angelina Cooper\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Kendra Bonner\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Nieves Gallegos! You have 2 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f151cd9d0e1b90e4150a10\",\n"
operator|+
literal|"    \"index\": 2,\n"
operator|+
literal|"    \"guid\": \"dfea2156-c940-43d3-a500-19b5b32719b3\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$2,684.11\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Virginia Watts\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"OPTIQUE\",\n"
operator|+
literal|"    \"email\": \"virginiawatts@optique.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (864) 547-3451\",\n"
operator|+
literal|"    \"address\": \"518 Crooke Avenue, Advance, Arkansas, 7719\",\n"
operator|+
literal|"    \"about\": \"ullamco exercitation excepteur mollit ad labore do\",\n"
operator|+
literal|"    \"registered\": \"2018-11-30T07:01:17 -03:00\",\n"
operator|+
literal|"    \"latitude\": -77.530698,\n"
operator|+
literal|"    \"longitude\": 174.424542,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"ex\",\n"
operator|+
literal|"      \"ad\",\n"
operator|+
literal|"      \"exercitation\",\n"
operator|+
literal|"      \"dolor\",\n"
operator|+
literal|"      \"aute\",\n"
operator|+
literal|"      \"ex\",\n"
operator|+
literal|"      \"Lorem\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Molly Blake\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Pearlie Dodson\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Montoya Watkins\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Virginia Watts! You have 4 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"apple\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f151cde376bf473f79cc97\",\n"
operator|+
literal|"    \"index\": 3,\n"
operator|+
literal|"    \"guid\": \"4f77bba3-531c-4450-b441-589bd19f2a57\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$3,381.01\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 22,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Walter Patrick\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"PEARLESEX\",\n"
operator|+
literal|"    \"email\": \"walterpatrick@pearlesex.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (954) 448-3420\",\n"
operator|+
literal|"    \"address\": \"387 Church Avenue, Geyserville, New Hampshire, 4849\",\n"
operator|+
literal|"    \"about\": \"cupidatat officia qui dolor veniam eu minim\",\n"
operator|+
literal|"    \"registered\": \"2016-12-04T05:12:01 -03:00\",\n"
operator|+
literal|"    \"latitude\": 83.816972,\n"
operator|+
literal|"    \"longitude\": -30.59895,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"eu\",\n"
operator|+
literal|"      \"Lorem\",\n"
operator|+
literal|"      \"ad\",\n"
operator|+
literal|"      \"ea\",\n"
operator|+
literal|"      \"adipisicing\",\n"
operator|+
literal|"      \"velit\",\n"
operator|+
literal|"      \"ex\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Pate Sweet\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Stein Burns\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Candy Swanson\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Walter Patrick! You have 5 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f151cd64d59419599bd15f\",\n"
operator|+
literal|"    \"index\": 4,\n"
operator|+
literal|"    \"guid\": \"2da48623-9b34-47ec-962e-400d45c8620a\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,891.52\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 37,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Ella Carey\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"PORTICO\",\n"
operator|+
literal|"    \"email\": \"ellacarey@portico.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (906) 400-3097\",\n"
operator|+
literal|"    \"address\": \"381 Bowne Street, Rose, Palau, 7582\",\n"
operator|+
literal|"    \"about\": \"voluptate pariatur magna occaecat elit magna excepteur\",\n"
operator|+
literal|"    \"registered\": \"2015-08-07T10:22:10 -03:00\",\n"
operator|+
literal|"    \"latitude\": 80.548898,\n"
operator|+
literal|"    \"longitude\": 67.575077,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"duis\",\n"
operator|+
literal|"      \"occaecat\",\n"
operator|+
literal|"      \"excepteur\",\n"
operator|+
literal|"      \"tempor\",\n"
operator|+
literal|"      \"excepteur\",\n"
operator|+
literal|"      \"Lorem\",\n"
operator|+
literal|"      \"proident\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Melendez Martin\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Haley Colon\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Emilia Schmidt\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Ella Carey! You have 8 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  }\n"
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonOver16k
parameter_list|()
block|{
name|testJson
argument_list|(
literal|"[\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f153352d42c0451f6f18a1\",\n"
operator|+
literal|"    \"index\": 0,\n"
operator|+
literal|"    \"guid\": \"4db9bc53-e652-47bd-8162-aad6b4280eb9\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$3,604.21\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 26,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Landry Malone\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"FROLIX\",\n"
operator|+
literal|"    \"email\": \"landrymalone@frolix.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (898) 454-3351\",\n"
operator|+
literal|"    \"address\": \"881 Strauss Street, Troy, Utah, 4374\",\n"
operator|+
literal|"    \"about\": \"ea et consequat fugiat est laboris sint\",\n"
operator|+
literal|"    \"registered\": \"2016-06-14T05:08:16 -03:00\",\n"
operator|+
literal|"    \"latitude\": 49.131275,\n"
operator|+
literal|"    \"longitude\": -171.114829,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"dolore\",\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"excepteur\",\n"
operator|+
literal|"      \"in\",\n"
operator|+
literal|"      \"sint\",\n"
operator|+
literal|"      \"exercitation\",\n"
operator|+
literal|"      \"cillum\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Palmer Pittman\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Clarice Wolfe\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Clements Battle\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Landry Malone! You have 9 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f153355ae6023dbfb7c2f8\",\n"
operator|+
literal|"    \"index\": 1,\n"
operator|+
literal|"    \"guid\": \"b4fdfc88-e367-42ba-ae1d-2f9f6a538986\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$1,909.36\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 24,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Nicholson Dodson\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"BUNGA\",\n"
operator|+
literal|"    \"email\": \"nicholsondodson@bunga.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (857) 550-2984\",\n"
operator|+
literal|"    \"address\": \"425 Thornton Street, Roosevelt, South Carolina, 4979\",\n"
operator|+
literal|"    \"about\": \"voluptate consequat consequat pariatur reprehenderit et exercitation\",\n"
operator|+
literal|"    \"registered\": \"2021-08-16T06:28:03 -03:00\",\n"
operator|+
literal|"    \"latitude\": -12.667348,\n"
operator|+
literal|"    \"longitude\": 84.401994,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"est\",\n"
operator|+
literal|"      \"in\",\n"
operator|+
literal|"      \"mollit\",\n"
operator|+
literal|"      \"id\",\n"
operator|+
literal|"      \"proident\",\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"qui\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Nelson Wolf\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Corina Fry\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Carlene Bean\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Nicholson Dodson! You have 8 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f15335f03974b701b22feb\",\n"
operator|+
literal|"    \"index\": 2,\n"
operator|+
literal|"    \"guid\": \"bf249929-ce37-4702-9671-1935278a2ff8\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,455.81\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"blue\",\n"
operator|+
literal|"    \"name\": \"Strickland Hodges\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"MACRONAUT\",\n"
operator|+
literal|"    \"email\": \"stricklandhodges@macronaut.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (806) 573-3642\",\n"
operator|+
literal|"    \"address\": \"929 Sutton Street, Caroleen, Delaware, 2273\",\n"
operator|+
literal|"    \"about\": \"culpa eiusmod commodo et officia aute exercitation\",\n"
operator|+
literal|"    \"registered\": \"2017-10-26T06:25:14 -03:00\",\n"
operator|+
literal|"    \"latitude\": 24.973892,\n"
operator|+
literal|"    \"longitude\": 50.218781,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"sit\",\n"
operator|+
literal|"      \"exercitation\",\n"
operator|+
literal|"      \"Lorem\",\n"
operator|+
literal|"      \"qui\",\n"
operator|+
literal|"      \"reprehenderit\",\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"cupidatat\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Trevino Howard\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Carol Frye\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Kara Parks\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Strickland Hodges! You have 7 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"apple\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f15335c8e74b04cc2cf3de\",\n"
operator|+
literal|"    \"index\": 3,\n"
operator|+
literal|"    \"guid\": \"450adca9-503b-41a2-b0a5-3ae7c9d68f81\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$3,895.80\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 30,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Judy Scott\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"ZENSOR\",\n"
operator|+
literal|"    \"email\": \"judyscott@zensor.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (961) 568-2876\",\n"
operator|+
literal|"    \"address\": \"359 Williamsburg Street, Venice, Ohio, 6487\",\n"
operator|+
literal|"    \"about\": \"in nulla adipisicing non culpa quis do\",\n"
operator|+
literal|"    \"registered\": \"2014-01-08T07:06:47 -04:00\",\n"
operator|+
literal|"    \"latitude\": -66.571697,\n"
operator|+
literal|"    \"longitude\": 92.502775,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"labore\",\n"
operator|+
literal|"      \"minim\",\n"
operator|+
literal|"      \"adipisicing\",\n"
operator|+
literal|"      \"nostrud\",\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"deserunt\",\n"
operator|+
literal|"      \"cupidatat\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Lorna Hines\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Farrell Ryan\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Georgette Elliott\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Judy Scott! You have 8 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f1533590ac45e8b03b433e\",\n"
operator|+
literal|"    \"index\": 4,\n"
operator|+
literal|"    \"guid\": \"88b4e813-d6dc-4902-b4f4-c28fca5d8b32\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$2,997.00\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"blue\",\n"
operator|+
literal|"    \"name\": \"Briggs Shields\",\n"
operator|+
literal|"    \"gender\": \"male\",\n"
operator|+
literal|"    \"company\": \"XLEEN\",\n"
operator|+
literal|"    \"email\": \"briggsshields@xleen.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (987) 435-3420\",\n"
operator|+
literal|"    \"address\": \"807 Vine Street, Callaghan, New Mexico, 7939\",\n"
operator|+
literal|"    \"about\": \"consectetur cupidatat anim pariatur adipisicing adipisicing irure\",\n"
operator|+
literal|"    \"registered\": \"2022-02-28T09:49:04 -03:00\",\n"
operator|+
literal|"    \"latitude\": 5.401627,\n"
operator|+
literal|"    \"longitude\": 64.076763,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"irure\",\n"
operator|+
literal|"      \"sint\",\n"
operator|+
literal|"      \"aliqua\",\n"
operator|+
literal|"      \"officia\",\n"
operator|+
literal|"      \"consectetur\",\n"
operator|+
literal|"      \"qui\",\n"
operator|+
literal|"      \"eiusmod\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Roberts Weeks\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Justice Bullock\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Simone Jacobson\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Briggs Shields! You have 8 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"strawberry\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f153359ee33fe36670766f\",\n"
operator|+
literal|"    \"index\": 5,\n"
operator|+
literal|"    \"guid\": \"f87bfc03-46d5-4e8f-9f55-dafa25e124cb\",\n"
operator|+
literal|"    \"isActive\": false,\n"
operator|+
literal|"    \"balance\": \"$2,933.39\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 27,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Kendra Peterson\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"FUELWORKS\",\n"
operator|+
literal|"    \"email\": \"kendrapeterson@fuelworks.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (951) 518-3222\",\n"
operator|+
literal|"    \"address\": \"155 Dewey Place, Westwood, Georgia, 1647\",\n"
operator|+
literal|"    \"about\": \"occaecat in ipsum non cillum proident officia\",\n"
operator|+
literal|"    \"registered\": \"2017-04-26T06:50:40 -03:00\",\n"
operator|+
literal|"    \"latitude\": -14.371127,\n"
operator|+
literal|"    \"longitude\": -0.400474,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"ad\",\n"
operator|+
literal|"      \"ipsum\",\n"
operator|+
literal|"      \"eiusmod\",\n"
operator|+
literal|"      \"cillum\",\n"
operator|+
literal|"      \"et\",\n"
operator|+
literal|"      \"et\",\n"
operator|+
literal|"      \"ipsum\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Orr Stone\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Mavis Mccullough\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Lea Whitfield\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Kendra Peterson! You have 6 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f15335cb2b3032b85383bb\",\n"
operator|+
literal|"    \"index\": 6,\n"
operator|+
literal|"    \"guid\": \"f1df8765-ebcb-40b9-957e-a04d0b16f2c6\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$2,219.05\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 38,\n"
operator|+
literal|"    \"eyeColor\": \"brown\",\n"
operator|+
literal|"    \"name\": \"Carissa Hogan\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"SOPRANO\",\n"
operator|+
literal|"    \"email\": \"carissahogan@soprano.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (919) 432-2299\",\n"
operator|+
literal|"    \"address\": \"555 Myrtle Avenue, Yonah, Arkansas, 4416\",\n"
operator|+
literal|"    \"about\": \"commodo exercitation ex adipisicing reprehenderit amet ut\",\n"
operator|+
literal|"    \"registered\": \"2019-04-02T12:22:13 -03:00\",\n"
operator|+
literal|"    \"latitude\": 26.396021,\n"
operator|+
literal|"    \"longitude\": -51.909653,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"deserunt\",\n"
operator|+
literal|"      \"veniam\",\n"
operator|+
literal|"      \"ut\",\n"
operator|+
literal|"      \"velit\",\n"
operator|+
literal|"      \"elit\",\n"
operator|+
literal|"      \"proident\",\n"
operator|+
literal|"      \"reprehenderit\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Mcknight Walsh\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Dalton Mclean\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Crystal Poole\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Carissa Hogan! You have 7 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  },\n"
operator|+
literal|"  {\n"
operator|+
literal|"    \"_id\": \"63f15335754d0e8d9275a344\",\n"
operator|+
literal|"    \"index\": 7,\n"
operator|+
literal|"    \"guid\": \"4f7f213b-29b1-48e6-b586-c76b609340f0\",\n"
operator|+
literal|"    \"isActive\": true,\n"
operator|+
literal|"    \"balance\": \"$1,114.53\",\n"
operator|+
literal|"    \"picture\": \"http://placehold.it/32x32\",\n"
operator|+
literal|"    \"age\": 34,\n"
operator|+
literal|"    \"eyeColor\": \"green\",\n"
operator|+
literal|"    \"name\": \"Vonda Whitley\",\n"
operator|+
literal|"    \"gender\": \"female\",\n"
operator|+
literal|"    \"company\": \"APEXTRI\",\n"
operator|+
literal|"    \"email\": \"vondawhitley@apextri.com\",\n"
operator|+
literal|"    \"phone\": \"+1 (852) 464-2850\",\n"
operator|+
literal|"    \"address\": \"115 Rogers Avenue, Mahtowa, Northern Mariana Islands, 8529\",\n"
operator|+
literal|"    \"about\": \"cupidatat consequat excepteur consequat incididunt officia esse\",\n"
operator|+
literal|"    \"registered\": \"2014-08-15T07:32:32 -04:00\",\n"
operator|+
literal|"    \"latitude\": -11.275146,\n"
operator|+
literal|"    \"longitude\": 114.522759,\n"
operator|+
literal|"    \"tags\": [\n"
operator|+
literal|"      \"occaecat\",\n"
operator|+
literal|"      \"occaecat\",\n"
operator|+
literal|"      \"incididunt\",\n"
operator|+
literal|"      \"ea\",\n"
operator|+
literal|"      \"et\",\n"
operator|+
literal|"      \"id\",\n"
operator|+
literal|"      \"eiusmod\"\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"friends\": [\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 0,\n"
operator|+
literal|"        \"name\": \"Tania Cunningham\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 1,\n"
operator|+
literal|"        \"name\": \"Boone Best\"\n"
operator|+
literal|"      },\n"
operator|+
literal|"      {\n"
operator|+
literal|"        \"id\": 2,\n"
operator|+
literal|"        \"name\": \"Nanette Yates\"\n"
operator|+
literal|"      }\n"
operator|+
literal|"    ],\n"
operator|+
literal|"    \"greeting\": \"Hello, Vonda Whitley! You have 1 unread messages.\",\n"
operator|+
literal|"    \"favoriteFruit\": \"banana\"\n"
operator|+
literal|"  }\n"
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonEmptyString
parameter_list|()
block|{
name|testJson
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testJson
parameter_list|(
name|String
name|jsonString
parameter_list|)
block|{
name|String
name|jsonStringMinified
init|=
name|JsonUtils
operator|.
name|normalize
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|testJsonVarchar
argument_list|(
name|jsonStringMinified
argument_list|)
expr_stmt|;
if|if
condition|(
name|unitDbAdapter
operator|.
name|supportsJsonType
argument_list|()
condition|)
block|{
name|testJsonOther
argument_list|(
name|jsonStringMinified
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testJsonOther
parameter_list|(
name|String
name|jsonString
parameter_list|)
block|{
name|JsonOther
name|jsonInsert
init|=
name|context
operator|.
name|newObject
argument_list|(
name|JsonOther
operator|.
name|class
argument_list|)
decl_stmt|;
name|jsonInsert
operator|.
name|setData
argument_list|(
operator|new
name|Json
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|jsonString
operator|.
name|isBlank
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|assertThrows
argument_list|(
name|CayenneRuntimeException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|context
operator|.
name|commitChanges
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|JsonOther
name|jsonSelect
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|SelectById
operator|.
name|query
argument_list|(
name|JsonOther
operator|.
name|class
argument_list|,
name|jsonInsert
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|jsonInsert
operator|.
name|getData
argument_list|()
argument_list|,
name|jsonSelect
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testJsonVarchar
parameter_list|(
name|String
name|jsonString
parameter_list|)
block|{
name|JsonVarchar
name|jsonInsert
init|=
name|context
operator|.
name|newObject
argument_list|(
name|JsonVarchar
operator|.
name|class
argument_list|)
decl_stmt|;
name|jsonInsert
operator|.
name|setData
argument_list|(
operator|new
name|Json
argument_list|(
name|jsonString
argument_list|)
argument_list|)
expr_stmt|;
comment|// In Oracle, an empty string is equivalent to NULL
if|if
condition|(
name|unitDbAdapter
operator|instanceof
name|OracleUnitDbAdapter
operator|&&
name|jsonString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|assertThrows
argument_list|(
name|CayenneRuntimeException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|context
operator|.
name|commitChanges
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|JsonVarchar
name|jsonSelect
init|=
name|context
operator|.
name|selectOne
argument_list|(
name|SelectById
operator|.
name|query
argument_list|(
name|JsonVarchar
operator|.
name|class
argument_list|,
name|jsonInsert
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|jsonInsert
operator|.
name|getData
argument_list|()
argument_list|,
name|jsonSelect
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

