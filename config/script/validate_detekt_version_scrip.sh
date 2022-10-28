#!/usr/bin/env bash

path_styleguide_md="Styleguide.md"
path_gradle_root="build.gradle"

function file_exist {
   local _path=$1
   if [ -f "$_path" ]; then
       echo "   --> file \"$_path\" exists."
   else
       echo "   --> file \"$_path\" does not exist."
       exit 1
   fi
}

function parse_yaml {
   local s='[[:space:]]*' w='[a-zA-Z0-9_]*' fs=$(echo @|tr @ '\034')
   # shellcheck disable=SC2207
   detekt_tmp_array=($(sed -ne "s|^\($s\):|\1|" \
        -e "s|^\($s\)\($w\)$s:$s[\"']\(.*\)[\"']$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s:$s\(.*\)$s\$|\1$fs\2$fs\3|p"  "$1" |
   awk -F$fs '{
      indent = length($1)/2;
      vname[indent] = $2;
      for (i in vname) {if (i > indent) {delete vname[i]}}
      if (length($3) > 0) {
         vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
         if ($2 == "active" && $3 == "true") {
            printf("%s%s=\"%s\"\n", vn, $2, $3);
         }
      }
   }'))
}
echo "-------- Start --------"
echo "-- Validate file path"
file_exist "$path_styleguide_md" || exit 1
file_exist "$path_gradle_root" || exit 1

echo "-- Get version from Styleguide.md"

# Steps:
# 1) Get only raw with `| detekt` -> "| detekt | 1.21.0 | https//:myUrl.com |"
# 2) Split string by `|` and get index 3 -> ` 1.21.0 `
# 3) Remove space before the string -> `1.21.0 `
# 4) Remove space after the string -> `1.21.0`
styleguide_version=$(grep -w "| detekt" ${path_styleguide_md} | cut -d '|' -f 3 | sed 's/ *$//g' | sed -e 's/^[[:space:]]*//')
echo "   --> Found version \"$styleguide_version\""

echo "-- Get version from build.gradle"
# Steps:
# 1) Get only raw with `detekt-gradle-plugin` -> `classpath 'io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0'`
# 2) Split string by `:` and get index 3 -> `1.21.0'`
# 3) Split string by `'` and get index 1  -> `1.21.0`
gradle_version=$(grep -w "detekt-gradle-plugin" ${path_gradle_root} | cut -d ':' -f 3 | cut -d "'" -f 1)
echo "   --> Found version \"$gradle_version\""

echo "-------- Result --------"
if [[ "$gradle_version" != "$styleguide_version" ]]; then
    echo "-- Fail:"
    echo "   --> Detekt version is not matching, please take a look at \"Styleguide.md\" and \"build.gradle\""
    echo "        - Styleguide.md: \"$styleguide_version\""
    echo "        - build.gradle: \"$gradle_version\""
    exit 1
else
    echo "-- Success:"
    echo "   --> Detekt is set at version \"$gradle_version\""
    exit 0
fi

