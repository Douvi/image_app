#!/usr/bin/env bash

path_styleguide_md="Styleguide.md"
path_detekt="config/detekt/detekt.yml"

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
file_exist "$path_detekt" || exit 1

echo "-- Get all Rules from Styleguide.md"

# Steps:
# 1) Get only raw with `TRUE` -> "| TRUE | [MyRule](https//:myUrl.com) | Details |"
# 2) Split string by `|` and get index 3 -> [MyRule](https//:myUrl.com)
# 3) Split string by `[` and get index 2 -> MyRule](https//:myUrl.com)
# 4) Split string by `]` and get index 1 -> MyRule
# 5) the whole list will be sorted
styleguide_md_array=( $(grep -w "TRUE" ${path_styleguide_md} | cut -d '|' -f 3 | cut -d '[' -f 2 | cut -d ']' -f 1 | sort) )
echo "   --> Found ${#styleguide_md_array[@]} rules active"

echo "-- Get all Rules from detekt.yml"
# Steps:
# 1) parse_yaml will general a array with all `active` line -> `connect_MyRule_active=true` or `connect_active=true`
# 2) Split string by `_` and get index 2 -> `MyRule` or `active`
# 3) Select only the one does not have the work active  -> MyRule
# 4) the whole list will be sorted
parse_yaml $path_detekt
# detekt_tmp_array will be create by parse_yaml 
detekt_array=( $(IFS=$'\n' ; echo "${detekt_tmp_array[*]}" | cut -d '_' -f 2 | grep -vw "active" | sort ))
echo "   --> Found ${#detekt_array[@]} rules active"

# We will check if both array are the same
result=( $(echo "${detekt_array[@]}" "${styleguide_md_array[@]}" | tr ' ' '\n' | sort | uniq -u))

echo "-------- Result --------"
if [[ ${#result[@]} != 0 ]]; then
    echo "-- Fail:"
    echo "   --> The set of rules are not matching, please take a look at \"Styleguide.md\" and \"config/detekt/detekt.yml\""
    echo "        Check Rule(s):"
    for element in "${result[@]}"
    do
      echo "            - $element"
    done
    exit 1
else
    echo "-- Success:"
    echo "   --> All rules are matching"
    exit 0
fi

