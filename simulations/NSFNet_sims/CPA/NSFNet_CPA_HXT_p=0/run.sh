for D in *; do
    if [ -d "${D}" ]; then
        java -jar snetsTCC.jar ${D}   # your processing here
    fi
done


