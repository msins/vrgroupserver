import re
import sys

with open(sys.argv[1], "r+") as f:
  data = "".join(f.readlines())
  print(data)
  data = re.sub(r'<property name="hibernate.connection.username" value="(.*?)"/>',
                '<property name="hibernate.connection.username" value="' +
                sys.argv[2] + '"/>', data)

  data = re.sub(r'<property name="hibernate.connection.password" value="(.*?)"/>',
                '<property name="hibernate.connection.password" value="' +
                sys.argv[3] + '"/>', data)
  print(data)
  f.seek(0)
  f.write(data)
  f.truncate()
