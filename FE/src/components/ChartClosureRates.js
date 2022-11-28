import React, { PureComponent } from "react";
import {
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";

export default class ChartClosureRates extends PureComponent {
  render() {
    const { info } = this.props;
    const closureData = [
      {
        name: "2018",
        폐업률: info?.closureRates.at(-5)
      },
      {
        name: "2019",
        폐업률: info?.closureRates.at(-4)
      },
      {
        name: "2020",
        폐업률: info?.closureRates.at(-3)
      },
      {
        name: "2021",
        폐업률: info?.closureRates.at(-2)
      },
      {
        name: "2022",
        폐업률: info?.closureRates.at(-1)
      }
    ];

    return (
      // <div>{info.closureRates.at(-1)}</div>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          width={500}
          height={300}
          data={closureData}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis domain={[0, 0.25]} />
          <Tooltip />
          <Legend />
          {/* <Bar dataKey="폐업률" fill="#8884d8" /> */}
          <Bar dataKey="폐업률" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    );
  }
}
